package com.github.ibole.infrastructure.cache.session.redis;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/*********************************************************************************************
 * .
 * 
 * 
 * <p>Copyright 2016, iBole Inc. All rights reserved.
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
