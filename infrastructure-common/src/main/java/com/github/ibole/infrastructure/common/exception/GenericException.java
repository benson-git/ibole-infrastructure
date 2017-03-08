package com.github.ibole.infrastructure.common.exception;

/*********************************************************************************************.
 * 
 * 
 * <p>Copyright 2016, iBole Inc. All rights reserved.
 * 
 * <p></p>
 *********************************************************************************************/


public class GenericException extends Exception {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  public GenericException(String msg, Throwable ex) {
    super(msg, ex);
  }
  
  public GenericException(Throwable ex) {
    super(ex);
  }

  public GenericException(String msg) {
    super(msg);
  }
}