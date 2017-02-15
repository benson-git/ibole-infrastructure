/**
 * 
 */
package io.ibole.infrastructure.security.key;

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
public class KeyStoreManagerException extends GenericException {
  

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  public KeyStoreManagerException(String msg, Throwable ex) {
    super(msg, ex);
  }
  
  public KeyStoreManagerException(Throwable ex) {
    super(ex);
  
  }

  public KeyStoreManagerException(String msg) {
    super(msg);
  }

}
