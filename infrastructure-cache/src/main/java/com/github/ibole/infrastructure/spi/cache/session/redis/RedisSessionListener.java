package com.github.ibole.infrastructure.spi.cache.session.redis;

/*********************************************************************************************.
 * 
 * 
 * <p>Copyright 2016, iBole Inc. All rights reserved.
 * 
 * <p>.</p>
 *********************************************************************************************/


public interface RedisSessionListener{
  /**
   * Session失效时监听
   * @param session RedisHttpSession
   */
  public void onInvalidated(RedisHttpSession session);
  
}
