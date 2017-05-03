package com.github.ibole.infrastructure.spi.cache.session.redis;

import com.google.common.base.Strings;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/*********************************************************************************************
 * .
 * 
 * 
 * <p>Copyright 2016, iBole Inc. All rights reserved.
 * 
 * <p>.
 * </p>
 *********************************************************************************************/


public class RedisSessionFilter implements Filter {
  // 静态资源不做过滤
  public static final String[] IGNORE_SUFFIX = {".css", ".js", ".png", ".jpg", ".gif", ".jpeg",
      ".bmp", ".ico", ".swf", ".psd", ".htc", ".htm", ".html", ".crx", ".xpi", ".exe", ".ipa",
      ".apk"};

  private RedisSessionManager sessionManager;

  /**
   * Called by the web container to indicate to a filter that it is being placed into service. The
   * servlet container calls the init method exactly once after instantiating the filter. The init
   * method must complete successfully before the filter is asked to do any filtering work. <br>
   */
  public void init(FilterConfig filterConfig) throws ServletException {
    String port = filterConfig.getInitParameter("port");
    String host = filterConfig.getInitParameter("host");
    String password = filterConfig.getInitParameter("password");
    String sessionTimeOutStr = filterConfig.getInitParameter("sessionTimeOut");
    int sessionTimeOut = 0;
    if (!Strings.isNullOrEmpty(sessionTimeOutStr)) {
      sessionTimeOut = Integer.parseInt(sessionTimeOutStr);
    }
    // CacheUtil.initCache(sessionTimeOut);
    this.sessionManager = new RedisSessionManager(host, port, password, sessionTimeOut);
  }

  /**
   * Set the instance of RedisSessionManager.
   * @param sessionManager RedisSessionManager to set
   */
  public void setSessionManager(RedisSessionManager sessionManager) {
    this.sessionManager = sessionManager;
  }

  /**
   * 
   * @see javax.servlet.Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
   * 
   */
  public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
      FilterChain filterChain) throws IOException, ServletException {
    HttpServletRequest request = (HttpServletRequest) servletRequest;

    if (isSkip(request)) {
      filterChain.doFilter(servletRequest, servletResponse);
      return;
    }
    HttpServletResponse response = (HttpServletResponse) servletResponse;

    RequestEventSubject eventSubject = new RequestEventSubject();
    // 生成Request包装器，替换原生的Request中的Session
    RedisHttpServletRequestWrapper requestWrapper =
        new RedisHttpServletRequestWrapper(request, response, this.sessionManager, eventSubject);
    try {
      // 向下一处理器传递替换后的Request
      filterChain.doFilter(requestWrapper, servletResponse);
    } finally {
      // 保存最新的Session
      eventSubject.completed(request, response);
    }
  }

  /**
   * 是否过滤请求.
   * 
   * @param request HttpServletRequest
   * @return true if it is static resource, otherwise, return false
   */
  private boolean isSkip(HttpServletRequest request) {
    String uri = request.getRequestURI().toLowerCase();
    for (String suffix : IGNORE_SUFFIX) {
      if (uri.endsWith(suffix)) {
        return true;
      }
    }
    return false;
  }

  public void destroy() {}
}
