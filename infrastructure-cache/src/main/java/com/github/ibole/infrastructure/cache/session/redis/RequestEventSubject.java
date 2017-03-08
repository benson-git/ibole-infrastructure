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
 * Request事件主题，主要是为了在请求结束后同步本地Session到Redis.
 *
 */
public class RequestEventSubject {
  private RequestEventObserver listener;

  public void attach(RequestEventObserver eventObserver) {
    this.listener = eventObserver;
  }

  public void detach() {
    this.listener = null;
  }

  /**
   * 请求结束后执行监听事件.
   * 
   * @param req HttpServletRequest
   * @param res HttpServletResponse
   */
  public void completed(HttpServletRequest req, HttpServletResponse res) {
    if (this.listener != null) {
      this.listener.completed(req, res);
    }
  }
}
