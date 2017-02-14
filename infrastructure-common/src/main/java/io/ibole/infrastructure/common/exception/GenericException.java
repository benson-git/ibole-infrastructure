package io.ibole.infrastructure.common.exception;

/*********************************************************************************************.
 * 
 * 
 * <p>版权所有：(c)2016， 深圳市拓润计算机软件开发有限公司
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