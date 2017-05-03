package com.github.ibole.infrastructure.spi.cache.redis.pubsub;

import com.github.ibole.infrastructure.spi.cache.redis.JedisUtil;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

/*********************************************************************************************.
 * 
 * 
 * <p>Copyright 2016, iBole Inc. All rights reserved.
 * 
 * <p>.</p>
 *********************************************************************************************/


public class SubClient {

  private Jedis jedis;//
  
  public SubClient(String host,int port, String password){
    jedis = JedisUtil.getInstance().getJedis(host, port, password);
  }
  
  public void sub(JedisPubSub listener,String channel){
      //jedis.subscribe(listener, channel);
      //此处将会阻塞，在client代码级别为JedisPubSub在处理消息时，将会“独占”链接
      //并且采取了while循环的方式，侦听订阅的消息
      //
    jedis.rpush("test1", "test1");
    jedis.rpush("test1", "test2");
    System.out.println(jedis.llen("test1"));
  }

}
