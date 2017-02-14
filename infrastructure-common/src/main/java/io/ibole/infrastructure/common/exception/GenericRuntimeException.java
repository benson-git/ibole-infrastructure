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
public class GenericRuntimeException extends RuntimeException {

  private static final long serialVersionUID = 232350690610319469L;
  
  public GenericRuntimeException(String msg, Throwable ex) {
    super(msg, ex);
  }
  
  public GenericRuntimeException(Throwable ex) {
    super(ex);
  }
}
