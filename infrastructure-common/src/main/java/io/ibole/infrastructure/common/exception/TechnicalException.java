package io.ibole.infrastructure.common.exception;

/*********************************************************************************************.
 * 
 * 
 * <p>版权所有：(c)2016， 深圳市拓润计算机软件开发有限公司
 * 
 * <p></p>
 *********************************************************************************************/


/**
 * /**
 * {@link ErrorReporter} in RuntimeException form, for propagating technical error code information via exceptions.

 * @author bwang (chikaiwang@hotmail.com)
 *
 */
public class TechnicalException extends RuntimeException {
  
  /**
   * 
   */
  private static final long serialVersionUID = -4679245578049447461L;

  private final ErrorReporter errorReporter;
  
  public TechnicalException(ErrorReporter errorReporter) {
    this.errorReporter = errorReporter;
  }
  
  public static TechnicalException fromErrorReporter(ErrorReporter errorReporter){
    return new TechnicalException(errorReporter);
  }

  /**
   * @return the errorReporter
   */
  public ErrorReporter getErrorReporter() {
    return errorReporter;
  }
  
}
