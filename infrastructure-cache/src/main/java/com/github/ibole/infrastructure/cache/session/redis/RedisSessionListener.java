package com.github.ibole.infrastructure.cache.session.redis;

/*********************************************************************************************.
 * 
 * 
 * <p>Copyright 2016, iBole Inc. All rights reserved.
 * 
 * <p></p>
 *********************************************************************************************/


public interface RedisSessionListener{
  /**
   * Session失效时监听
   * @param session
   */
  public void onInvalidated(RedisHttpSession session);
  
}
