package com.github.ibole.infrastructure.web.exception;

import javax.servlet.http.HttpServletRequest;

/**
 * 关于异常的工具类.
 */
public class Exceptions {

  /**
   * 将CheckedException转换为UncheckedException.
   * @param e Exception
   * @return RuntimeException RuntimeException
   */
  public static RuntimeException unchecked(Exception e) {
    if (e instanceof RuntimeException) {
      return (RuntimeException) e;
    } else {
      return new RuntimeException(e);
    }
  }
  
  /**
   * 在request中获取异常类.
   * 
   * @param request HttpServletRequest
   * @return error exception Throwable
   */
  public static Throwable getThrowable(HttpServletRequest request) {
    Throwable ex = null;
    if (request.getAttribute("exception") != null) {
      ex = (Throwable) request.getAttribute("exception");
    } else if (request.getAttribute("javax.servlet.error.exception") != null) {
      ex = (Throwable) request.getAttribute("javax.servlet.error.exception");
    }
    return ex;
  }

}
