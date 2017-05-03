package com.github.ibole.infrastructure.spi.cache.redis;

import redis.clients.jedis.Jedis;

/*********************************************************************************************
 * .
 * 
 * 
 * <p>Copyright 2016, iBole Inc. All rights reserved.
 * 
 * <p>.
 * </p>
 *********************************************************************************************/


public interface RedisCallback<T> {
  public T call(Jedis jedis, Object params);
}
