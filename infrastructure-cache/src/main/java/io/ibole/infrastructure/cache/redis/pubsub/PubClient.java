package io.ibole.infrastructure.cache.redis.pubsub;

import io.ibole.infrastructure.cache.redis.JedisUtil;

import redis.clients.jedis.Jedis;

/*********************************************************************************************.
 * 
 * 
 * <p>版权所有：(c)2016， 深圳市拓润计算机软件开发有限公司
 * 
 * <p></p>
 *********************************************************************************************/


public class PubClient {

  private Jedis jedis;//
  public PubClient(String host,int port, String password){
      jedis = JedisUtil.getInstance().getJedis(host, port, password);
  }
  
  public void pub(String channel,String message){
      jedis.publish(channel, message);
      jedis.rpush(channel, message);
  }
  
  public void close(String channel){
      jedis.publish(channel, "quit");
      jedis.del(channel);//
  }

}
