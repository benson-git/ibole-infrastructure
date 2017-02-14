/**
 * 
 */
package io.ibole.infrastructure.security.jwt;

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
public class RefreshTokenNotFoundException extends RuntimeException {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  public RefreshTokenNotFoundException(String message)
  {
      super(message);
  }

  public RefreshTokenNotFoundException(String message, Throwable cause)
  {
      super(message, cause);
  }

  public RefreshTokenNotFoundException(Throwable cause) {
      super(cause);
  }
}
