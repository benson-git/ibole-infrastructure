package io.ibole.infrastructure.cache.session.redis;

/*********************************************************************************************.
 * 
 * 
 * <p>版权所有：(c)2016， 深圳市拓润计算机软件开发有限公司
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
