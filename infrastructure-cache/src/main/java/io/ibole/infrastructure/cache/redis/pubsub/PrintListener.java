package io.ibole.infrastructure.cache.redis.pubsub;

import org.apache.commons.lang3.time.DateFormatUtils;

import java.util.Date;

import redis.clients.jedis.JedisPubSub;

/*********************************************************************************************.
 * 
 * 
 * <p>版权所有：(c)2016， 深圳市拓润计算机软件开发有限公司
 * 
 * <p></p>
 *********************************************************************************************/


public class PrintListener extends JedisPubSub{

  @Override
  public void onMessage(String channel, String message) {
      String time = DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss");
      System.out.println("message receive:" + message + ",channel:" + channel + "..." + time);
      //此处我们可以取消订阅
      if(message.equalsIgnoreCase("quit")){
          this.unsubscribe(channel);
      }
  }
}
