package com.github.ibole.infrastructure.cache.redis.pubsub;

import com.github.ibole.infrastructure.cache.redis.JedisUtil;

import redis.clients.jedis.Jedis;

/*********************************************************************************************.
 * 
 * 
 * <p>Copyright 2016, iBole Inc. All rights reserved.
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
