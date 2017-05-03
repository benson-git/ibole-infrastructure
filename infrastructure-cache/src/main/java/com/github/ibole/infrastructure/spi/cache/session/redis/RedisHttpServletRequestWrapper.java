package com.github.ibole.infrastructure.spi.cache.session.redis;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/*********************************************************************************************
 * .
 * 
 * 
 * <p>Copyright 2016, iBole Inc. All rights reserved.
 * 
 * <p>.
 * </p>
 *********************************************************************************************/


public class RedisHttpServletRequestWrapper extends HttpServletRequestWrapper {
  private HttpServletResponse response;
  private RedisHttpSession httpSession;
  private RedisSessionManager sessionManager;
  private RequestEventSubject requestEventSubject;

  public RedisHttpServletRequestWrapper(HttpServletRequest request, HttpServletResponse response,
      RedisSessionManager sessionManager, RequestEventSubject requestEventSubject) {
    super(request);
    this.response = response;
    this.sessionManager = sessionManager;
    this.requestEventSubject = requestEventSubject;
  }

  @Override
  public HttpSession getSession(boolean create) {
    if ((this.httpSession != null) && (!(this.httpSession.expired))) {
      return this.httpSession;
    }
    this.httpSession =
        this.sessionManager.createSession(this, this.response, this.requestEventSubject, create);
    return this.httpSession;
  }

  @Override
  public HttpSession getSession() {
    return getSession(true);
  }
}
