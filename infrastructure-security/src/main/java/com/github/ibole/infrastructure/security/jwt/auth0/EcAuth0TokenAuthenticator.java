/*
 * Copyright 2016-2017 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package com.github.ibole.infrastructure.security.jwt.auth0;

import com.github.ibole.infrastructure.common.exception.MoreThrowables;
import com.github.ibole.infrastructure.common.utils.Constants;
import com.github.ibole.infrastructure.security.jwt.BaseTokenAuthenticator;
import com.github.ibole.infrastructure.security.jwt.JwtObject;
import com.github.ibole.infrastructure.security.jwt.RefreshTokenNotFoundException;
import com.github.ibole.infrastructure.security.jwt.TokenHandlingException;
import com.github.ibole.infrastructure.security.jwt.TokenStatus;
import com.github.ibole.infrastructure.spi.cache.redis.RedisSimpleTempalte;

import com.google.common.base.Preconditions;
import com.google.common.base.Stopwatch;
import com.google.common.base.Strings;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.util.concurrent.TimeUnit;

/*********************************************************************************************
 * .
 * 
 * 
 * <p>Copyright 2016, iBole Inc. All rights reserved.
 * 
 * <p>
 * </p>
 *********************************************************************************************/


/**
 * Token lifecycle management (create/renew/validate/revoke).
 *  
 * @author bwang
 *
 */
public class EcAuth0TokenAuthenticator extends BaseTokenAuthenticator {
  private ECPublicKey ecPublicKey;
  private ECPrivateKey ecPrivateKey;

  /**
   * @param redisTemplate
   */
  public EcAuth0TokenAuthenticator(RedisSimpleTempalte redisTemplate) {
    super(redisTemplate);
    KeyPairGenerator keyPairGenerator;
    try {
      keyPairGenerator = KeyPairGenerator.getInstance("EC");
      keyPairGenerator.initialize(256);
      KeyPair keyPair = keyPairGenerator.generateKeyPair();
      ecPublicKey = (ECPublicKey) keyPair.getPublic();
      ecPrivateKey = (ECPrivateKey) keyPair.getPrivate();
    } catch (NoSuchAlgorithmException e) {
      MoreThrowables.throwIfUnchecked(e);
    }

  }

  /**
   * Create Access Token.
   */
  @Override
  public String createAccessToken(JwtObject claimObj)
      throws TokenHandlingException {
    Preconditions.checkArgument(claimObj != null, "Parameter claimObj cannot be null");
    String token = null;
    if (!Constants.ANONYMOUS_ID.equalsIgnoreCase(claimObj.getLoginId()) && !getRedisTemplate()
        .exists(getRefreshTokenKey(claimObj.getLoginId()))) {
      throw new RefreshTokenNotFoundException("Refresh token not found.");
    }
    token = Auth0Utils.createJwtWithECKey(claimObj, ecPublicKey, ecPrivateKey);
    getRedisTemplate().hset(getRefreshTokenKey(claimObj.getLoginId()), Constants.ACCESS_TOKEN, token);
    return token;
  }
  
  /**
   * Create Refresh Token.
   */
  @Override
  public String createRefreshToken(JwtObject claimObj) throws TokenHandlingException {
    Preconditions.checkArgument(claimObj != null, "Parameter claimObj cannot be null");
    String token = null;
    token = Auth0Utils.createJwtWithECKey(claimObj, ecPublicKey, ecPrivateKey);
    getRedisTemplate().hset(getRefreshTokenKey(claimObj.getLoginId()), Constants.REFRESH_TOKEN, token);
    getRedisTemplate().hset(getRefreshTokenKey(claimObj.getLoginId()), Constants.CLIENT_ID, claimObj.getClientId());
    getRedisTemplate().expire(getRefreshTokenKey(claimObj.getLoginId()), claimObj.getTtlSeconds());
    return token;
  } 
  
  /**
    * ##Token验证流程图：
    * ```sequence
    * Client->Server: 传递Token
    * Server->Redis: 以Token为键取值
    * Redis->Client: Token不存在，返回错误信息
    * Redis->Server: Token存在，则以Token为键取值并返回
    * Server->Client: 验证Token的合法性,判断是否被篡改或者盗用，返回JSON信息```
   */
  @Override
  public TokenStatus validAccessToken(String token, String clientId, String loginId) {
    Preconditions.checkArgument(!Strings.isNullOrEmpty(token), "Token cannot be null");
    TokenStatus status = TokenStatus.VALIDATED;
    final Stopwatch stopwatch = Stopwatch.createStarted();
    // validate the token signature.
    status = Auth0Utils.validateToken(token, clientId, ecPublicKey, ecPrivateKey);
    // check if it is a anonymous access token, anonymous don't have refresh token
    if (Constants.ANONYMOUS_ID.equalsIgnoreCase(loginId)) {
      return status;
    }

    // As it is expensive to frequently check the refresh token (from redis),
    // here we just do it when the access token is expired.
    if (status.isExpired()) {
      String refreshToken = getRedisTemplate().hget(getRefreshTokenKey(loginId), Constants.REFRESH_TOKEN);
      // check if the refresh token is expired
      if (Strings.isNullOrEmpty(refreshToken)) {
        status = TokenStatus.REFRESH_TOKEN_EXPIRED;
      } else {
        // if the same login id logon in different client.
        // Check if the both client id and login id are match with the provided token.
        String previousClientId = getRedisTemplate().hget(getRefreshTokenKey(loginId), Constants.CLIENT_ID);
        if (!clientId.equals(previousClientId)) {
          status = TokenStatus.INVALID;
        }
      }
    }

    if (status.isInvalid()) {
      revokeRefreshToken(clientId, loginId);
      return status;
    }

    String elapsedString = Long.toString(stopwatch.elapsed(TimeUnit.MILLISECONDS));
    logger.debug("JwtUtils.validateToken elapsed time: {} ms", elapsedString);
    return status;
  }
  
  @Override
  public String renewAccessToken(String token, int ttlSeconds, boolean refreshToken) throws TokenHandlingException {
    Preconditions.checkArgument(!Strings.isNullOrEmpty(token), "Token cannot be null");
    String newToken;
    JwtObject jwtObj = Auth0Utils.claimsOfTokenWithoutValidation(token, ecPublicKey, ecPrivateKey);
    jwtObj.setTtlSeconds(ttlSeconds);
    if (refreshToken) {
      newToken = createRefreshToken(jwtObj);
    } else {
      newToken = createAccessToken(jwtObj);
    }
    return newToken;
  }
  
  @Override
  public void revokeRefreshToken(String clientId, String loginId) {
    getRedisTemplate().del(getRefreshTokenKey(loginId));
  }
  
  private String getRefreshTokenKey(String loginId) {
    return Constants.REFRESH_TOKEN_KEY_PREFIX + loginId;
  }
  
}
