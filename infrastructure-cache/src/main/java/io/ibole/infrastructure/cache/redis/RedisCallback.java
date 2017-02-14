package io.ibole.infrastructure.cache.redis;

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


public interface RedisCallback<T> {
  public T call(Jedis jedis, Object params);
}
