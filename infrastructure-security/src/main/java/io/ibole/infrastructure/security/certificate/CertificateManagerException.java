/**
 * 
 */
package io.ibole.infrastructure.security.certificate;

import io.ibole.infrastructure.common.exception.GenericException;

/*********************************************************************************************.
 * 
 * 
 * <p>Copyright 2016, iBole Inc. All rights reserved.
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
