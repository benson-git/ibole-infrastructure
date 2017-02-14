package io.ibole.infrastructure.security.jwt;

/*********************************************************************************************.
 * 
 * 
 * <p>版权所有：(c)2016， 深圳市拓润计算机软件开发有限公司
 * 
 * <p></p>
 *********************************************************************************************/


/**
 * @author bwang
 *
 */
public class TokenParseException extends Exception {
  
  /**
   * 
   */
  private static final long serialVersionUID = 4704645465359901261L;

  public TokenParseException(String message)
  {
      super(message);
  }

  public TokenParseException(String message, Throwable cause)
  {
      super(message, cause);
  }

  public TokenParseException(Throwable cause) {
      super(cause);
  }
}
