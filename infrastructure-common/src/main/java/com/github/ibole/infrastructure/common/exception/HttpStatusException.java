package com.github.ibole.infrastructure.common.exception;

/*********************************************************************************************
 * .
 * 
 * 
 * <p>Copyright 2016, iBole Inc. All rights reserved.
 * 
 * <p>
 * </p>
 *********************************************************************************************/


/**
 * @author bwang (chikaiwang@hotmail.com)
 *
 */
public class HttpStatusException extends Exception {
  private static final long serialVersionUID = 1L;

  /* the HTTP error code */
  private final int errorCode;

  /**
   * Constructor.
   *
   * @param errorCode the HTTP status code for this exception
   * @param msg human readable message
   * @param cause reason for this exception
   */
  public HttpStatusException(final int errorCode, final String msg, final Throwable cause) {
    super(msg, cause);
    this.errorCode = errorCode;
  }

  /**
   * Constructor.
   *
   * @param errorCode the HTTP status code for this exception
   * @param cause reason for this exception
   */
  public HttpStatusException(final int errorCode, final Throwable cause) {
    super(String.valueOf(errorCode), cause);
    this.errorCode = errorCode;
  }

  /**
   * Constructor.
   *
   * @param errorCode the HTTP status code for this exception
   * @param msg human readable message
   */
  public HttpStatusException(final int errorCode, final String msg) {
    super(msg);
    this.errorCode = errorCode;
  }

  /**
   * Return the HTTP status code for this exception.
   *
   * @return the HTTP status code
   */
  public int getErrorCode() {
    return errorCode;
  }
}
