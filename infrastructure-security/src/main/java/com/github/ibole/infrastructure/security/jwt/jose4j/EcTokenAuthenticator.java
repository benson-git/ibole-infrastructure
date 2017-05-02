package com.github.ibole.infrastructure.security.jwt.jose4j;
import com.github.ibole.infrastructure.cache.redis.RedisSimpleTempalte;
import com.github.ibole.infrastructure.common.utils.Constants;
import com.github.ibole.infrastructure.security.jwt.BaseTokenAuthenticator;
import com.github.ibole.infrastructure.security.jwt.JwtObject;
import com.github.ibole.infrastructure.security.jwt.RefreshTokenNotFoundException;
import com.github.ibole.infrastructure.security.jwt.TokenParseException;
import com.github.ibole.infrastructure.security.jwt.TokenStatus;

import com.google.common.base.Stopwatch;
import com.google.common.base.Strings;

import org.jose4j.jwk.EllipticCurveJsonWebKey;
import org.jose4j.lang.JoseException;

import java.util.concurrent.TimeUnit;

/*********************************************************************************************
 * .
 * 
 * 
 * <p>
 * Copyright 2016, iBole Inc. All rights reserved.
 * 
 * <p>
 * </p>
 *********************************************************************************************/


/**
 *  Token实现类 - EllipticCurve 算法
 * 
 * @author bwang (chikaiwang@hotmail.com)
 *
 */
public class EcTokenAuthenticator extends BaseTokenAuthenticator<EllipticCurveJsonWebKey> {

  public EcTokenAuthenticator(RedisSimpleTempalte redisTemplate) {
    super(redisTemplate);
  }
  
  /**
   * Create Access Token.
   * #Token存储流程（以登录为例）：
   * Client->Server: 发送数据（用户名密码及验证码）
   * Server->Redis: 合法：生成Token,并以loginId:token键值对存储
   * Server->Client: 不合法：返回错误信息```
   * #Token生成
   * @throws TokenParseException 
   */
  @Override
  public String createAccessToken(JwtObject claimObj, EllipticCurveJsonWebKey pSenderJwk)
      throws TokenParseException {
    String token = null;
    try {
      if (!Constants.ANONYMOUS_ID.equalsIgnoreCase(claimObj.getLoginId()) && !getRedisTemplate()
          .exists(getRefreshTokenKey(claimObj.getLoginId()))) {
        throw new RefreshTokenNotFoundException("Refresh token not found.");
      }
      token = JwtUtils.createJwtWithECKey(claimObj, pSenderJwk);
      getRedisTemplate().hset(getRefreshTokenKey(claimObj.getLoginId()), Constants.ACCESS_TOKEN, token);
    } catch (JoseException ex) {
      logger.error("Error happened when generating the jwt token.", ex);
      throw new TokenParseException(ex);
    }
    return token;
  }
  
  /**
   * Create Refresh Token.
   * #Token存储流程（以登录为例）：
   * Client->Server: 发送数据（用户名密码及验证码）
   * Server->Redis: 合法：生成Token,并以loginId:token键值对存储
   * Server->Client: 不合法：返回错误信息```
   * #Token生成
   */
  @Override
  public String createRefreshToken(JwtObject claimObj, EllipticCurveJsonWebKey pSenderJwk) throws TokenParseException {
    String token = null;
    try {
      
      token = JwtUtils.createJwtWithECKey(claimObj, pSenderJwk);
      getRedisTemplate().hset(getRefreshTokenKey(claimObj.getLoginId()), Constants.REFRESH_TOKEN, token);
      getRedisTemplate().hset(getRefreshTokenKey(claimObj.getLoginId()), Constants.CLIENT_ID, claimObj.getClientId());
      getRedisTemplate().expire(getRefreshTokenKey(claimObj.getLoginId()), claimObj.getTtlSeconds());

    } catch (JoseException ex) {
         logger.error("Error happened when generating the jwt token.", ex);
         throw new TokenParseException(ex);
    }
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
  public TokenStatus validAccessToken(String token, String clientId, String loginId, EllipticCurveJsonWebKey pSenderJwk) { 
    TokenStatus status = TokenStatus.VALIDATED;
    try {
      if (!Strings.isNullOrEmpty(token)) {
        final Stopwatch stopwatch = Stopwatch.createStarted();
        // validate the token signature.
        boolean validateFlag = JwtUtils.validateToken(token, clientId, loginId, pSenderJwk);
        String elapsedString = Long.toString(stopwatch.elapsed(TimeUnit.MILLISECONDS));
        logger.info("JwtUtils.validateToken elapsed time: {} ms", elapsedString);
        if (validateFlag) {
          // check if the token is expired.
          final Stopwatch stopwatch1 = Stopwatch.createStarted();
          status = validateTokenExpired(token, clientId, loginId, pSenderJwk);
          String elapsedString1 = Long.toString(stopwatch1.elapsed(TimeUnit.MILLISECONDS));
          logger.info("JwtUtils.validateTokenExpired elapsed time: {} ms", elapsedString1);
        } else {
          status = TokenStatus.INVALID;
        }
      } else {
        status = TokenStatus.INVALID;
      }
    } catch (TokenParseException ex) {
      logger.error("Invalid token '{}' for '{}:{}'.", token, loginId, clientId, ex);
      status = TokenStatus.INVALID;
    }
    return status;
  }
  /**
   *  Check if the refresh token is expired except for anonymous token.
   * @param token
   * @param clientId
   * @param loginId
   * @param pSenderJwk
   * @param pReceiverJwk
   * @return TokenStatus
   */
  private TokenStatus validateTokenExpired(String token, String clientId, String loginId,
      EllipticCurveJsonWebKey pSenderJwk) {
    TokenStatus status = TokenStatus.VALIDATED;
    if (JwtUtils.isExpired(token, loginId, pSenderJwk)) {
      status = TokenStatus.ACCESS_TOKEN_EXPIRED;
      if (Constants.ANONYMOUS_ID.equalsIgnoreCase(loginId)) {
        return TokenStatus.ACCESS_TOKEN_EXPIRED;
      }
      String refreshToken = getRedisTemplate().hget(Constants.REFRESH_TOKEN_KEY_PREFIX + loginId,
          Constants.REFRESH_TOKEN);

      // check if the refresh token is expired
      if (Strings.isNullOrEmpty(refreshToken)) {
        status = TokenStatus.REFRESH_TOKEN_EXPIRED;
      }
    } else {
      // if the same login id logon in different client.
      // Check if the both client id and login id are match with the provided token.
      String previousClientId = getRedisTemplate().hget(getRefreshTokenKey(loginId), Constants.CLIENT_ID);
      if (!clientId.equals(previousClientId)) {
        status = TokenStatus.REFRESH_TOKEN_EXPIRED;
      }
    }
    return status;
  }
  
  @Override
  public String renewAccessToken(String token, int ttlSeconds, boolean refreshToken,
      EllipticCurveJsonWebKey pSenderJwk)
      throws TokenParseException {

    String newToken;
    JwtObject jwtObj = JwtUtils.claimsOfTokenWithoutValidation(token);
    jwtObj.setTtlSeconds(ttlSeconds);
    if (refreshToken) {
      newToken = createRefreshToken(jwtObj, pSenderJwk);
    } else {
      newToken = createAccessToken(jwtObj, pSenderJwk);
    }
    return newToken;
  }
  
  private String getRefreshTokenKey(String loginId) {
    return Constants.REFRESH_TOKEN_KEY_PREFIX + loginId;
  }
  
}
