/**
 * 
 */
package io.ibole.infrastructure.security.certificate;

import io.ibole.infrastructure.common.exception.GenericException;

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
public class CertificateManagerException extends GenericException {
  

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  public CertificateManagerException(String msg, Throwable ex) {
    super(msg, ex);
  }
  
  public CertificateManagerException(Throwable ex) {
    super(ex);
  
  }

  public CertificateManagerException(String msg) {
    super(msg);
  }

}
