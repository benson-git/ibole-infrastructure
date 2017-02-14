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
public class TokenSignatureException extends Exception {


  /**
   * 
   */
  private static final long serialVersionUID = -4232725203751990825L;

  public TokenSignatureException(String message)
  {
      super(message);
  }

  public TokenSignatureException(String message, Throwable cause)
  {
      super(message, cause);
  }
  
  public TokenSignatureException(Throwable cause) {
    super(cause);
}
}
