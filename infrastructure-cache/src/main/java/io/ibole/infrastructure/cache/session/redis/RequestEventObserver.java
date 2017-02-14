package io.ibole.infrastructure.cache.session.redis;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/*********************************************************************************************
 * .
 * 
 * 
 * <p>版权所有：(c)2016， 深圳市拓润计算机软件开发有限公司
 * 
 * <p>
 * </p>
 *********************************************************************************************/

/**
 * Request事件观察者，在每次请求开始时注入到RequestWrapper中.
 *
 */
public interface RequestEventObserver {
  public void completed(HttpServletRequest req, HttpServletResponse res);
}
