package io.ibole.infrastructure.cache.redis.pubsub;

import org.apache.commons.lang3.RandomStringUtils;

/*********************************************************************************************.
 * 
 * 
 * <p>Copyright 2016, iBole Inc. All rights reserved.
 * 
 * <p></p>
 *********************************************************************************************/


public class PubSubTestMain {

  /**
   * @param args
   */
  public static void main(String[] args) throws Exception{
      PPubClient pubClient = new PPubClient("139.219.236.163", 6379, "TOPrank123");
      final String channel = "pubsub-channel-p";
      final PPSubClient subClient = new PPSubClient("139.219.236.163", 6379, "TOPrank123", "subClient-1");
      
      Thread subThread = new Thread(new Runnable() {
          
          @Override
          public void run() {
              System.out.println("----------subscribe operation begin-------");
              //在API级别，此处为轮询操作，直到unsubscribe调用，才会返回
              subClient.sub(channel);
              System.out.println("----------subscribe operation end-------");
              
          }
      });
      subThread.setDaemon(true);
      subThread.start();
      Thread.sleep(2000000);
      int i = 0;
      while(i < 2){
          String message = RandomStringUtils.random(6, true, true);//apache-commons
          pubClient.pub(channel, message);
          i++;
          Thread.sleep(1000);
      }
      subClient.unsubscribe(channel);
  }
  
}
