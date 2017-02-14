/**
 * 
 */
package io.ibole.infrastructure.common.exception;

/*********************************************************************************************.
 * 
 * 
 * <p>版权所有：(c)2016， 深圳市拓润计算机软件开发有限公司
 * 
 * <p></p>
 *********************************************************************************************/


/**
 * @author bwang (chikaiwang@hotmail.com)
 *
 */
public class HttpStatusException extends Exception {
  private static final long serialVersionUID = 1L;

  /* the HTTP error code */
  private final int errorCode;

  /**
   * Constructor.
   *
   * @param errorCode the HTTP status code for this exception
   * @param msg human readable message
   * @param cause reason for this exception
   */
  public HttpStatusException(final int errorCode, final String msg, final Throwable cause) {
      super(msg, cause);
      this.errorCode = errorCode;
  }

  /**
   * Constructor.
   *
   * @param errorCode the HTTP status code for this exception
   * @param cause reason for this exception
   */
  public HttpStatusException(final int errorCode, final Throwable cause) {
      super(String.valueOf(errorCode), cause);
      this.errorCode = errorCode;
  }

  /**
   * Constructor.
   *
   * @param errorCode the HTTP status code for this exception
   * @param msg human readable message
   */
  public HttpStatusException(final int errorCode, final String msg) {
      super(msg);
      this.errorCode = errorCode;
  }

  /**
   * Return the HTTP status code for this exception.
   *
   * @return the HTTP status code
   */
  public int getErrorCode() {
      return errorCode;
  }
}
