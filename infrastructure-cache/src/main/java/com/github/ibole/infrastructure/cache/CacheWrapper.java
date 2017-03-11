package com.github.ibole.infrastructure.cache;

/*********************************************************************************************.
 * 
 * 
 * <p>Copyright 2016, iBole Inc. All rights reserved.
 * 
 * <p>.</p>
 *********************************************************************************************/


public interface CacheWrapper<K, V> {
  
  void put(K key, V value);

  V get(K key);

}
