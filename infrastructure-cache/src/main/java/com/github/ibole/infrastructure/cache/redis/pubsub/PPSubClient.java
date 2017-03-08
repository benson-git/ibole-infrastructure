package com.github.ibole.infrastructure.cache.redis.pubsub;

import com.github.ibole.infrastructure.cache.redis.JedisUtil;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

/*********************************************************************************************.
 * 
 * 
 * <p>Copyright 2016, iBole Inc. All rights reserved.
 * 
 * <p></p>
 *********************************************************************************************/


public class PPSubClient {

    private Jedis jedis;//
    private JedisPubSub listener;//单listener
    
    public PPSubClient(String host,int port, String password,String clientId){
        jedis = JedisUtil.getInstance().getJedis(host, port, password);;
        listener = new PPrintListener(clientId, new Jedis(host, port));
    }
    
    public void sub(String channel){
        jedis.subscribe(listener, channel);
    }
    
    public void unsubscribe(String channel){
        listener.unsubscribe(channel);
    }
    
}
