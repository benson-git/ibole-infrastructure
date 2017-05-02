///*
// * Copyright 2016-2017 the original author or authors.
// *
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// *      http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//
//package com.github.ibole.infrastructure.security.jwt.auth0;
//
//import com.github.ibole.infrastructure.cache.redis.RedisSimpleTempalte;
//import com.github.ibole.infrastructure.security.jwt.BaseTokenAuthenticator;
//import com.github.ibole.infrastructure.security.jwt.JwtObject;
//import com.github.ibole.infrastructure.security.jwt.TokenParseException;
//import com.github.ibole.infrastructure.security.jwt.TokenStatus;
//
//import java.security.interfaces.ECKey;
//
///*********************************************************************************************.
// * 
// * 
// * <p>Copyright 2016, iBole Inc. All rights reserved.
// * 
// * <p></p>
// *********************************************************************************************/
//
//
///**
// * @author bwang
// *
// */
//public class HmacTokenAuthenticator extends BaseTokenAuthenticator<ECKey> {
//
//  /**
//   * @param redisTemplate
//   */
//  public HmacTokenAuthenticator(RedisSimpleTempalte redisTemplate) {
//    super(redisTemplate);
//  }
//
//  /**
//   * Create new access token base on the claim object.
//   * @param claim JwtObject
//   * @param refreshToken boolean
//   * @param key 
//   * @return generated token
//   * @throws TokenParseException 
//   */
//  public String createAccessToken(JwtObject claim, K key) throws TokenParseException {
//    
//  }
//  
//  /**
//   * Create new refresh token base on the claim object.
//   * @param claim JwtObject
//   * @param refreshToken boolean
//   * @param key 
//   * @return generated token
//   * @throws TokenParseException 
//   */
//  public String createRefreshToken(JwtObject claim, K key) throws TokenParseException;
//  
//  /**
//   * Revoke a refresh token base on the claim object.
//   */
//  public void revokeRefreshToken(String clientId, String loginId) throws TokenParseException;
//  /**
//   * Renew token base on the old token.
//   * @param token the old token to be renewed
//   * @param ttlSeconds the time to live 
//   * @param refreshToken if it is a refresh token
//   * @param key
//   * @return the new token
//   * @throws TokenParseException
//   */
//  public String renewAccessToken(String token, int ttlSeconds, boolean refreshToken, K key) throws TokenParseException;
//
//  /**
//   * 验证Access Token.
//   */
//  public TokenStatus validAccessToken(String token, String clientId, String loginId, K key);
//  /**
//   * 验证Refresh Token.
//   */
//  public boolean validRefreshToken(String token, String clientId, String loginId, K key);
//}
