package com.github.ibole.infrastructure.spi.cache.redis;

import com.github.ibole.infrastructure.common.serialization.KryoSerializationUtil;
import com.github.ibole.infrastructure.spi.cache.CacheAdapter;

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
 * @author bwang
 *
 */
public class RedisCache extends CacheAdapter {

  private RedisSimpleTempalte redisTemplate;

  public RedisCache(RedisSimpleTempalte redisTemplate) {
    this.redisTemplate = redisTemplate;
  }
  /*
   * (non-Javadoc)
   * 
   * @see com.github.ibole.infrastructure.spi.cache.CacheAdapter#doGet(java.lang.String)
   */
  @Override
  protected <T> T doGet(String key) {
    return KryoSerializationUtil.getInstance().deserialize(
        redisTemplate.getSafetyByte(key));
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.github.ibole.infrastructure.spi.cache.CacheAdapter#doSet(int, java.lang.String,
   * java.lang.Object)
   */
  @Override
  protected <T> void doSet(int ttlSeconds, String key, T value) {
    redisTemplate
        .setSafety(key, KryoSerializationUtil.getInstance().serialize(value), ttlSeconds);

  }

  /*
   * (non-Javadoc)
   * 
   * @see com.github.ibole.infrastructure.spi.cache.CacheAdapter#doRemove(java.lang.String)
   */
  @Override
  protected void doRemove(String key) {
    redisTemplate.del(key);

  }
  
}
