package com.github.ibole.infrastructure.security.jwt;

import com.github.ibole.infrastructure.spi.cache.redis.RedisSimpleTempalte;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*********************************************************************************************
 * .
 * 
 * 
 * <p>
 * Copyright 2016, iBole Inc. All rights reserved.
 * 
 * <p>
 * </p>
 * 
 *********************************************************************************************/

/**
 * Base class for simple JWT authenticators.
 * 
 * Create a token would be to authenticate the user via their login credentials, and if successful
 * return a token corresponding to that user
 * 
 * @author bwang (chikaiwang@hotmail.com)
 *
 * @param <S>
 * @param <R>
 */
public class BaseTokenAuthenticator implements TokenAuthenticator {

  protected Logger logger = LoggerFactory.getLogger(this.getClass());

  protected RedisSimpleTempalte redisTemplate;

  public BaseTokenAuthenticator(RedisSimpleTempalte redisTemplate) {
    this.redisTemplate = redisTemplate;
  }

  @Override
  public String createAccessToken(JwtObject claim) throws TokenHandlingException {
    throw new UnsupportedOperationException();
  }


  @Override
  public TokenStatus validAccessToken(String token, String clientId) {
    throw new UnsupportedOperationException();
  }

  @Override
  public String createRefreshToken(JwtObject claim) throws TokenHandlingException {
    throw new UnsupportedOperationException();
  }

  @Override
  public TokenStatus validRefreshToken(String token, String clientId) {
    throw new UnsupportedOperationException();
  }

  @Override
  public String renewAccessToken(String refreshToken, int ttlSeconds)
      throws TokenHandlingException {
    throw new UnsupportedOperationException();
  }

  /**
   * @return the redisTemplate
   */
  public RedisSimpleTempalte getRedisTemplate() {
    return redisTemplate;
  }

  @Override
  public void revokeRefreshToken(String loginId) {
    throw new UnsupportedOperationException();
  }



}
