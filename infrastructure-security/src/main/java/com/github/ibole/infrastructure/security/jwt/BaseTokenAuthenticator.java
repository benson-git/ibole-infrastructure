package com.github.ibole.infrastructure.security.jwt;

import com.github.ibole.infrastructure.cache.redis.RedisSimpleTempalte;

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
 *  Create a token would be to authenticate the user via their login credentials, 
 *  and if successful return a token corresponding to that user
 * 
 * @author bwang (chikaiwang@hotmail.com)
 *
 * @param <S>
 * @param <R>
 */
public class BaseTokenAuthenticator<K> implements TokenAuthenticator<K> {

  protected Logger logger = LoggerFactory.getLogger(this.getClass());

  protected RedisSimpleTempalte redisTemplate;

  public BaseTokenAuthenticator(RedisSimpleTempalte redisTemplate) {
    this.redisTemplate = redisTemplate;
  }

  @Override
  public String createAccessToken(JwtObject claim, K key) throws TokenParseException {
    throw new UnsupportedOperationException();
  }


  @Override
  public TokenStatus validAccessToken(String token, String clientId, String loginId, K key) {
    throw new UnsupportedOperationException();
  }

  @Override
  public String createRefreshToken(JwtObject claim, K key) throws TokenParseException{
    throw new UnsupportedOperationException();
  }

  @Override
  public boolean validRefreshToken(String token, String clientId, String loginId, K key) {
    throw new UnsupportedOperationException();
  }
  
  @Override
  public String renewAccessToken(String token, int ttlSeconds, boolean refreshToken, K key) throws TokenParseException {
    throw new UnsupportedOperationException();
  }

  /**
   * @return the redisTemplate
   */
  public RedisSimpleTempalte getRedisTemplate() {
    return redisTemplate;
  }

  @Override
  public void revokeRefreshToken(String clientId, String loginId)
      throws TokenParseException {
    throw new UnsupportedOperationException();
  }




}
