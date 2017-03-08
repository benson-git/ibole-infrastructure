package com.github.ibole.infrastructure.cache.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;

/*********************************************************************************************
 * .
 * 
 * 
 * <p>Copyright 2016, iBole Inc. All rights reserved.
 * 
 * <p>
 * </p>
 *********************************************************************************************/

public abstract class RedisTemplate {
  
  protected final Logger logger = LoggerFactory.getLogger(this.getClass());

  /*
   * Execute callback operation.
   * 
   * @param params parameters to the call
   */
  protected <T> T execute(RedisCallback<T> callback, Object... args) {
    Jedis jedis = getRedis();
    try {
      return callback.call(jedis, args);
    } catch (Exception e) {
      logger.error("Execute callback failed!", e);
    } finally {
      if (jedis != null) {
        closeRedis(jedis);
      }
    }
    return null;
  }
  
  protected abstract Jedis getRedis();
  
  protected abstract void closeRedis(Jedis jedis);
  
}
