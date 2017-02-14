package io.ibole.infrastructure.cache.redis;

import io.ibole.infrastructure.cache.CacheWrapper;
import io.ibole.infrastructure.common.serialization.KryoSerializationUtil;

/*********************************************************************************************.
 * 
 * 
 * <p>版权所有：(c)2016， 深圳市拓润计算机软件开发有限公司
 * 
 * <p></p>
 *********************************************************************************************/


/**
 * @author bwang
 *
 */
public class RedisCacheWrapper<K, V> implements CacheWrapper<K, V> {

  private RedisSimpleTempalte redisTemplate;

  public RedisCacheWrapper(RedisSimpleTempalte redisTemplate) {
    this.redisTemplate = redisTemplate;
  }

  @Override
  public void put(K key, V value) {
    redisTemplate
        .setSafety(key.toString(), KryoSerializationUtil.getInstance().serialize(value), 0);
  }

  @Override
  public V get(K key) {

    return KryoSerializationUtil.getInstance().deserialize(
        redisTemplate.getSafetyByte(key.toString()));
  }
}
