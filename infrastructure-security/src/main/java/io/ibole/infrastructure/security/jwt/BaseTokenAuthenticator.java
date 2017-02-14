package io.ibole.infrastructure.security.jwt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.ibole.infrastructure.cache.redis.RedisSimpleTempalte;

/*********************************************************************************************
 * .
 * 
 * 
 * <p>
 * 版权所有：(c)2016， 深圳市拓润计算机软件开发有限公司
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
public class BaseTokenAuthenticator<S, R> implements TokenAuthenticator<S, R> {

  protected Logger logger = LoggerFactory.getLogger(this.getClass());

  protected RedisSimpleTempalte redisTemplate;

  public BaseTokenAuthenticator(RedisSimpleTempalte redisTemplate) {
    this.redisTemplate = redisTemplate;
  }

  @Override
  public String createAccessToken(JwtObject claim, S sender, R receiver) throws TokenParseException {
    throw new UnsupportedOperationException();
  }


  @Override
  public TokenStatus validAccessToken(String token, String clientId, String loginId, S sender,
      R receiver) {
    throw new UnsupportedOperationException();
  }

  @Override
  public String createRefreshToken(JwtObject claim, S sender, R receiver) throws TokenParseException{
    throw new UnsupportedOperationException();
  }

  @Override
  public boolean validRefreshToken(String token, String clientId, String loginId, S sender,
      R receiver) {
    throw new UnsupportedOperationException();
  }
  
  @Override
  public String renewToken(String token, int ttlSeconds, boolean refreshToken, S sender,
      R receiver) throws TokenParseException {
    throw new UnsupportedOperationException();
  }

  /**
   * @return the redisTemplate
   */
  public RedisSimpleTempalte getRedisTemplate() {
    return redisTemplate;
  }




}
