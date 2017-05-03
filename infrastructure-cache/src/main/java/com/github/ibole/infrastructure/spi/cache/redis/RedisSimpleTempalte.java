package com.github.ibole.infrastructure.spi.cache.redis;

import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import redis.clients.jedis.GeoCoordinate;
import redis.clients.jedis.GeoRadiusResponse;
import redis.clients.jedis.GeoUnit;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;

/*********************************************************************************************
 * .
 *   
 * 
 * <p>
 * Copyright 2016, iBole Inc. All rights reserved.
 * 
 * <p>.
 * </p>
 *********************************************************************************************/


public class RedisSimpleTempalte extends RedisTemplate {

  private String ip;
  private int port;
  private String password;
  // key Prefix to distinguish different domain.
  private String namespace = "default";

  public RedisSimpleTempalte(String ip, int port, String password) {
    this.ip = ip;
    this.port = port;
    this.password = password;
  }

  /**
   * Constructor.
   * 
   * @param ip IP
   * @param port Port
   * @param password Password
   * @param namespace Name space to distinguish different domain
   */
  public RedisSimpleTempalte(String ip, int port, String password, String namespace) {
    this.ip = ip;
    this.port = port;
    this.namespace = namespace;
    this.password = password;
  }

  private String buildKey(String pKey) {
    return namespace + "." + pKey;
  }

  public void changeNamespace(String namespace) {
    this.namespace = namespace;
  }

  public String select(int index) {
    return execute(new RedisCallback<String>() {
      public String call(Jedis jedis, Object parms) {
        return jedis.select(index);
      }
    }, index);
  }

  public Set<String> keys(String pattern) {
    return execute(new RedisCallback<Set<String>>() {
      public Set<String> call(Jedis jedis, Object parms) {
        return jedis.keys(buildKey(pattern));
      }
    }, pattern);
  }

  public String hget(String key, String field) {
    return execute(new RedisCallback<String>() {
      public String call(Jedis jedis, Object parms) {
        return jedis.hget(buildKey(key), field);
      }
    }, key, field);
  }

  public void hset(String key, String field, String value) {
    execute(new RedisCallback<String>() {
      public String call(Jedis jedis, Object parms) {
        // String key = ((Object[]) parms)[0].toString();
        // String field = ((Object[]) parms)[1].toString();
        // String value = ((Object[]) parms)[2].toString();
        jedis.hset(buildKey(key), field, value);
        return null;
      }
    }, key, field, value);
  }
  
  public void expire(String key, int seconds) {
    execute(new RedisCallback<String>() {
      public String call(Jedis jedis, Object parms) {
        jedis.expire(buildKey(key), seconds);   
        return null;
      }
    }, key, seconds);
  }
  /**
   * @param key String
   * @return value String
   */
  public String get(String key) {
    return execute(new RedisCallback<String>() {
      public String call(Jedis jedis, Object parms) {
        return jedis.get(buildKey(key));
      }
    }, key);
  }

  public String get(String key, LoadData load, boolean isUpdateCache) {
    String value = get(key);
    if (value == null || value.equals("")) {
      value = load.loadDbData();
      if ((value == null || "".equals(value)) && isUpdateCache) {
        set(key, value);
      }
    }
    return value;
  }

  public byte[] getByte(String key) {
    return execute(new RedisCallback<byte[]>() {
      public byte[] call(Jedis jedis, Object parms) {
        try {
          return jedis.get(buildKey(key).getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
          logger.error("Error happened when get byte[] by key {}", key, e);
        }
        return null;
      }
    }, key);
  }

  public byte[] getByte(String key, LoadData load, boolean isUpdateCache) {
    return execute(new RedisCallback<byte[]>() {
      public byte[] call(Jedis jedis, Object parms) {
        try {
          byte[] b = jedis.get(buildKey(key).getBytes("UTF-8"));
          if (b == null || b.length == 0) {
            b = load.loadDbData();
            if ((b != null && b.length > 0) && isUpdateCache) {
              jedis.set(buildKey(key).getBytes("UTF-8"), b);
            }
          }
          return b;
        } catch (UnsupportedEncodingException e) {
          logger.error("Error happened when get byte[] by key {}", key, e);
        }
        return null;
      }
    }, key);
  }

  public void set(String key, String value) {
    execute(new RedisCallback<String>() {
      public String call(Jedis jedis, Object parms) {
        jedis.set(buildKey(key), value);
        return null;
      }
    }, key, value);
  }

  public void set(String key, byte[] value) {
    execute(new RedisCallback<String>() {
      public String call(Jedis jedis, Object parms) {
        try {
          jedis.set(buildKey(key).getBytes("UTF-8"), value);
        } catch (UnsupportedEncodingException e) {
          logger.error("Error happened when save byte[] with key {}", key, e);
        }
        return null;
      }
    }, key, value);
  }

  // seconds:过期时间（单位：秒）
  public void set(String key, String value, int seconds) {
    execute(new RedisCallback<String>() {
      public String call(Jedis jedis, Object parms) {
        jedis.setex(buildKey(key), seconds, value);
        return null;
      }
    }, key, value, seconds);
  }

  public void set(String key, byte[] value, int seconds) {
    execute(new RedisCallback<String>() {
      public String call(Jedis jedis, Object parms) {
        try {
          jedis.setex(buildKey(key).getBytes("UTF-8"), seconds, value);
        } catch (UnsupportedEncodingException e) {
          logger.error("Error happened when save byte[] with key {} and expired time {}", key, seconds, e);
        }
        return null;
      }
    }, key, value, seconds);
  }

  public void del(String key) {
    execute(new RedisCallback<String>() {
      public String call(Jedis jedis, Object parms) {
        jedis.del(key);
        return null;
      }
    }, key);
  }

  public Boolean exists(String key) {
    return execute(new RedisCallback<Boolean>() {
      public Boolean call(Jedis jedis, Object parms) {
        return jedis.exists(buildKey(key));
      }
    }, key);
  }

  public String llen(String key) {
    return execute(new RedisCallback<String>() {
      public String call(Jedis jedis, Object parms) {
        return jedis.llen(buildKey(key)) + "";
      }
    }, key);
  }

  public void lpush(String key, String value) {
    execute(new RedisCallback<String>() {
      public String call(Jedis jedis, Object parms) {
        jedis.lpush(buildKey(key), value);
        return null;
      }
    }, key, value);
  }

  public void lpush(String key, byte[] value) {
    execute(new RedisCallback<String>() {
      public String call(Jedis jedis, Object parms) {
        try {
          jedis.lpush(buildKey(key).getBytes("UTF-8"), value);
        } catch (UnsupportedEncodingException e) {
          logger.error("Error happened when save byte[] with key {}", key, e);
        }
        return null;
      }
    }, key, value);
  }

  public void rpush(String key, String value) {
    execute(new RedisCallback<String>() {
      public String call(Jedis jedis, Object parms) {
        jedis.rpush(buildKey(key), value);
        return null;
      }
    }, key, value);
  }

  public void rpush(String key, byte[] value) {
    execute(new RedisCallback<String>() {
      public String call(Jedis jedis, Object parms) {
        try {
          jedis.rpush(buildKey(key).getBytes("UTF-8"), value);
        } catch (UnsupportedEncodingException e) {
          logger.error("Error happened when save byte[] with key {}", key, e);
        }
        return null;
      }
    }, key, value);
  }

  public void lpushPipeLine(String key, List<String> values) {
    execute(new RedisCallback<String>() {
      @SuppressWarnings("unchecked")
      public String call(Jedis jedis, Object parms) {
        Pipeline p = jedis.pipelined();
        String buildKey = buildKey(key);
        for (String value : values) {
          p.lpush(buildKey, value);
        }
        p.sync();
        return null;
      }
    }, key, values);
  }

  public void lpushPipeLineByte(String key, List<byte[]> values) {
    execute(new RedisCallback<byte[]>() {
      @SuppressWarnings("unchecked")
      public byte[] call(Jedis jedis, Object parms) {
        Pipeline p = jedis.pipelined();
        try {
          byte[] byteKey = buildKey(key).getBytes("UTF-8");
          for (byte[] value : values) {
            p.lpush(byteKey, value);
          }
          p.sync();
        } catch (UnsupportedEncodingException e) {
          logger.error("Error happened when save byte[] with key {}", key, e);
        }
        return null;
      }
    }, key, values);
  }

  public void rpushPipeLineByte(String key, List<byte[]> values) {
    execute(new RedisCallback<byte[]>() {
      @SuppressWarnings("unchecked")
      public byte[] call(Jedis jedis, Object parms) {
        Pipeline p = jedis.pipelined();
        try {
          byte[] byteKey = buildKey(key).getBytes("UTF-8");
          for (byte[] value : values) {
            p.rpush(byteKey, value);
          }
          p.sync();
        } catch (UnsupportedEncodingException e) {
          logger.error("Error happened when save byte[] with key {}", key, e);
        }
        return null;
      }
    }, key, values);
  }

  public void rpushPipeLine(String key, List<String> values) {
    execute(new RedisCallback<String>() {
      @SuppressWarnings("unchecked")
      public String call(Jedis jedis, Object parms) {
        Pipeline p = jedis.pipelined();
        String buildKey = buildKey(key);
        for (String value : values) {
          p.rpush(buildKey, value);
        }
        p.sync();
        return null;
      }
    }, key, values);
  }

  public List<String> lrange(String key, long start, long end) {
    return execute(new RedisCallback<List<String>>() {
      public List<String> call(Jedis jedis, Object parms) {
        return jedis.lrange(buildKey(key), start, end);
      }
    }, key, start, end);
  }

  public List<byte[]> lrangeByte(String key, long start, long end) {
    return execute(new RedisCallback<List<byte[]>>() {
      public List<byte[]> call(Jedis jedis, Object parms) {
        try {
          return jedis.lrange(buildKey(key).getBytes("UTF-8"), start, end);
        } catch (UnsupportedEncodingException e) {
          logger.error("Error happened when save byte[] with key {}", key, e);
        }
        return null;
      }
    }, key, start, end);
  }

  public List<String> lrange(String key, long start, long end, LoadData load, boolean isCache) {
    List<String> list = lrange(key, start, end);
    if ((list == null || list.isEmpty()) && load != null) {
      list = load.loadDbData();
      if (!list.isEmpty() && isCache)
        lpushPipeLine(key, list);
    }
    return list;
  }

  public void incr(String key) {
    execute(new RedisCallback<String>() {
      public String call(Jedis jedis, Object parms) {
        jedis.incr(buildKey(key));
        return null;
      }
    }, key);
  }

  public void sadd(String key, String value) {
    execute(new RedisCallback<String>() {
      public String call(Jedis jedis, Object parms) {
        jedis.sadd(buildKey(key), value);
        return null;
      }
    }, key, value);
  }

  public void saddPipeLine(String key, Collection<String> values) {
    execute(new RedisCallback<String>() {
      public String call(Jedis jedis, Object parms) {
        Pipeline p = jedis.pipelined();
        String buildKey = buildKey(key);
        for (String val : values) {
          p.sadd(buildKey, val);
        }
        p.sync();
        return null;
      }
    }, key, values);
  }

  public Set<String> smembers(String key) {
    return execute(new RedisCallback<Set<String>>() {
      public Set<String> call(Jedis jedis, Object parms) {
        return jedis.smembers(buildKey(key));
      }
    }, key);
  }

  public Set<String> sinter(String... keys) {
    return execute(new RedisCallback<Set<String>>() {
      public Set<String> call(Jedis jedis, Object parms) {
        return jedis.sinter(keys);
      }
    }, "");
  }

  public Long sinterstore(String dstkey, String... keys) {
    return execute(new RedisCallback<Long>() {
      public Long call(Jedis jedis, Object parms) {
        return jedis.sinterstore(buildKey(dstkey), keys);
      }
    }, dstkey);
  }

  public List<String> brpop(String key) {
    return execute(new RedisCallback<List<String>>() {
      public List<String> call(Jedis jedis, Object parms) {
        return jedis.brpop(0, buildKey(key));
      }
    }, key);
  }

  public Long geoadd(String key, double longitude, double latitude, String member) {
    return execute(new RedisCallback<Long>() {
      public Long call(Jedis jedis, Object parms) {
        return jedis.geoadd(buildKey(key), longitude, latitude, member);
      }
    }, key);
  }

  public void geoaddPipeLine(String key, List<Map<String, GeoCoordinate>> memberCoordinateList) {
    execute(new RedisCallback<Long>() {
      public Long call(Jedis jedis, Object parms) {
        Pipeline p = jedis.pipelined();
        for (Map<String, GeoCoordinate> memberCoordinateMap : memberCoordinateList) {
          p.geoadd(buildKey(key), memberCoordinateMap);
        }
        p.sync();
        return null;
      }
    }, key);
  }

  public List<GeoCoordinate> geopos(String key, String... members) {
    return execute(new RedisCallback<List<GeoCoordinate>>() {
      public List<GeoCoordinate> call(Jedis jedis, Object parms) {
        return jedis.geopos(buildKey(key), members);
      }
    }, key);
  }

  public Double geodist(String key, String member1, String member2, GeoUnit unit) {
    return execute(new RedisCallback<Double>() {
      public Double call(Jedis jedis, Object parms) {
        return jedis.geodist(buildKey(key), member1, member2, unit);
      }
    }, key);
  }

  public List<GeoRadiusResponse> georadius(String key, double longitude, double latitude, double radius, GeoUnit unit) {
    return execute(new RedisCallback<List<GeoRadiusResponse>>() {
      public List<GeoRadiusResponse> call(Jedis jedis, Object parms) {
        return jedis.georadius(buildKey(key), longitude, latitude, radius, unit);
      }
    }, key);
  }

  public List<String> geohash(String key, String... members) {
    return execute(new RedisCallback<List<String>>() {
      public List<String> call(Jedis jedis, Object parms) {
        return jedis.geohash(buildKey(key), members);
      }
    }, key);
  }

  /**
   * Redis高并发下获取数据. 该方法尝试解决下面两个问题： 1. 缓存雪崩 2. 缓存过期的短时间内存在数据库短暂压力问题 方案：
   * 以两个键值对的缓存代替之前的一个，将缓存时间（key-time),和缓存数据（key-data）分离，这样 1.当缓存过期时，第一个线程发现key-time没有，则先更新key-time,
   * 2.然后去查询数据库（或任何比较耗时的数据查询方式），并更新key-data的值，
   * 3.当后续线程来获取数据时，虽然第一个还没有从数据库查完并更新缓存，但发现key-time存在，会获取旧的数据。
   * 
   * @param key the key to retrieve data
   * @return the array of byte
   */
  public byte[] getSafetyByte(String key) {
    return execute(new RedisCallback<byte[]>() {
      public byte[] call(Jedis jedis, Object parms) {
        try {
          // 缓存过期 && 获取锁成功
          // setnx:原子操作，如果不存在则设置值，并返回1。如果缓存存在，则返回0，设置缓存失败
          if (jedis.setnx(buildKey("lock_" + key), System.currentTimeMillis() + "") == 1) {
            /**
             * 将锁的有效时间设为10s，在10s内如果查询数据库成功，则更新该锁的失效时间=缓存时间。 如果60s内出现异常，则60s后第一个请求又会去访问数据库...
             * 返回null表示没有查询到数据库，外层代码会通过数据库获取数据
             */
            jedis.expire("lock_" + key, 10);
            return null;

            // (缓存未过期) || (缓存过期，但是获取锁失败) then 返回旧的数据
          } else {
            return jedis.get(buildKey(key).getBytes("UTF-8"));
          }
        } catch (UnsupportedEncodingException e) {
          logger.error("Error happened when get byte[] by key {}", key, e);
        }
        return null;
      }
    }, key);
  }

  /**
   * Redis高并发下存储数据.
   * 
   * @param key the key to store data
   * @param value the array of byte
   * @param seconds the time to live
   */
  public void setSafety(String key, byte[] value, int seconds) {
    execute(new RedisCallback<String>() {
      public String call(Jedis jedis, Object parms) {
        try {
          if (seconds > 0) {
            // 添加缓存，缓存有效时间=真实时间+1天
            jedis.setex(buildKey(key).getBytes("UTF-8"), seconds + 60 * 60 * 24, value);
            // 添加缓存锁，有效时间 =真实时间
            jedis.setex(buildKey("lock_" + key), seconds, System.currentTimeMillis() + "");
          } else {
            jedis.set(buildKey(key).getBytes("UTF-8"), value);
            jedis.setex(buildKey("lock_" + key), seconds, System.currentTimeMillis() + "");
          }
        } catch (UnsupportedEncodingException e) {
          logger.error("Error happened when save byte[] with key {}", key, e);
        }
        return null;
      }
    }, key, value);
  }

  @Override
  protected Jedis getRedis() {

    return JedisUtil.getInstance().getJedis(ip, port, password);
  }

  @Override
  protected void closeRedis(Jedis jedis) {

    JedisUtil.getInstance().closeJedis(jedis, ip, port, password);
  }

}
