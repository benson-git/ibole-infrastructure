package com.github.ibole.infrastructure.cache.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

/*********************************************************************************************
 * .
 * 
 * 
 * <p>Copyright 2016, iBole Inc. All rights reserved.
 * 
 * <p>
 * </p>
 *********************************************************************************************/

 
public class JedisUtil {
  private Logger logger = LoggerFactory.getLogger(this.getClass());
  
  private static ConcurrentHashMap<String, JedisPool> maps = new ConcurrentHashMap<String, JedisPool>();

  private JedisUtil() {}

  private static class RedisUtilHolder {
    private static final JedisUtil instance = new JedisUtil();
  }

  public static JedisUtil getInstance() {
    return RedisUtilHolder.instance;  
  }
 
  private static JedisPool getPool(String ip, int port, String password) {
    String key = ip + ":" + port;
    JedisPool pool;
    if (!maps.containsKey(key)) {
      JedisPoolConfig config = new JedisPoolConfig();
      config.setMaxTotal(RedisConfig.MAX_ACTIVE);
      config.setMaxIdle(RedisConfig.MAX_IDLE);
      config.setMaxWaitMillis(RedisConfig.MAX_WAIT);
      config.setTestOnBorrow(true);
      config.setTestOnReturn(true);
      pool = new JedisPool(config, ip, port,RedisConfig.TIMEOUT, password);
      maps.putIfAbsent(key, pool);
    } else {
      pool = maps.get(key);
    }
    return pool;
  }
  
  /**
   * Get Jedis by specified ip and port.
   */
  public Jedis getJedis(String ip, int port, String password) {
    Jedis jedis = null;
    int count = 0;
    do {
      try {
        ++count;
        jedis = getPool(ip, port, password).getResource();
      } catch (Exception e) {
        logger.error("get redis master {}:{} failed!", ip, port, e);
        //避免在finally中再次关闭reids
        getPool(ip, port, password).returnBrokenResource(jedis);
      }
    } while (jedis == null && count < RedisConfig.RETRY_NUM);
    return jedis;
  }

  /**
   * Close Jedis by specified ip and port.
   */
  public void closeJedis(Jedis jedis, String ip, int port, String password) {
    if (jedis != null) {
      getPool(ip, port, password).returnResource(jedis);
    }
  }

  /**
   * Destory all JedisPool store in cache.
   */
  public void destoryAll() {
    Iterator<String> its = maps.keySet().iterator();
    while (its.hasNext()) {
      String key = its.next();
      maps.get(key).destroy();
    }
  }
  
  /**
   * Destory JedisPool by specified ip and port.
   */
  public void destory(String ip, int port) {
    maps.get(ip + ":" + port).destroy();

  }
}
