package com.github.ibole.infrastructure.security.jwt;

/*********************************************************************************************.
 * 
 * 
 * <p>Copyright 2016, iBole Inc. All rights reserved.
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
