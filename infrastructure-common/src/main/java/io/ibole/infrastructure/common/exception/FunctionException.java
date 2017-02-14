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
 * {@link ErrorReporter} in Exception form, for propagating business error code information via exceptions.
 * @author bwang (chikaiwang@hotmail.com)
 *
 */
public class FunctionException extends Exception {
  
  private static final long serialVersionUID = 7524730598134055506L;
  
  private final ErrorReporter errorReporter;
 
  public FunctionException(ErrorReporter errorReporter) {
    this.errorReporter = errorReporter;
  }

  /**
   * @return the errorReporter
   */
  public ErrorReporter getErrorReporter() {
    return errorReporter;
  }
  
  public static FunctionException fromErrorReporter(ErrorReporter errorReporter){
    return new FunctionException(errorReporter);
  }
  
  /**
   * Extract an error {@link ErrorStatus} from the causal chain of a {@link Throwable}.
   * If no status can be found, a status is created with {@link Code#UNKNOWN} as its code and
   * {@code t} as its cause.
   *
   * @return non-{@code null} status
   */
//  public static ErrorStatus fromThrowable(Throwable t) {
//    Throwable cause = checkNotNull(t);
//    while (cause != null) {
//      if (cause instanceof StatusException) {
//        return ((StatusException) cause).getStatus();
//      } else if (cause instanceof StatusRuntimeException) {
//        return ((StatusRuntimeException) cause).getStatus();
//      }
//      cause = cause.getCause();
//    }
//    // Couldn't find a cause with a ErrorStatus
//    return UNKNOWN.withCause(t);
//  }
}
