/**
 * 
 */
package com.github.ibole.infrastructure.common.exception;

/*********************************************************************************************
 * .
 * 
 * 
 * <p>
 * Copyright 2016, iBole Inc. All rights reserved.
 * 
 * <p>
 * </p>
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
