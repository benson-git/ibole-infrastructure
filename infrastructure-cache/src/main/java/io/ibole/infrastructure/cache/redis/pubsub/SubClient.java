package io.ibole.infrastructure.cache.redis.pubsub;

import io.ibole.infrastructure.cache.redis.JedisUtil;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

/*********************************************************************************************.
 * 
 * 
 * <p>版权所有：(c)2016， 深圳市拓润计算机软件开发有限公司
 * 
 * <p></p>
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
