package io.ibole.infrastructure.cache.session.redis;

/*********************************************************************************************
 * .
 * 
 * 
 * <p>版权所有：(c)2016， 深圳市拓润计算机软件开发有限公司
 * 
 * <p>
 * </p>
 *********************************************************************************************/


public class SessionException extends RuntimeException {

  private static final long serialVersionUID = -4742795005659710148L;

  public SessionException() {}

  public SessionException(String message) {
    super(message);
  }

  public SessionException(String message, Throwable cause) {
    super(message, cause);
  }

  public SessionException(Throwable cause) {
    super(cause);
  }
}
