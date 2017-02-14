package io.ibole.infrastructure.cache.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;

/*********************************************************************************************
 * .
 * 
 * 
 * <p>版权所有：(c)2016， 深圳市拓润计算机软件开发有限公司
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
