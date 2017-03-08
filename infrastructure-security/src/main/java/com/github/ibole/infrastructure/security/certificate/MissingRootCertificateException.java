package com.github.ibole.infrastructure.security.certificate;

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
 * Signal not initialized root CA certificate store.
 */
public class MissingRootCertificateException extends IllegalStateException {

  private static final long serialVersionUID = -9087082417871920302L;

  public MissingRootCertificateException() {
    super();
  }
 
  public MissingRootCertificateException(String message, Throwable cause) {
    super(message, cause);
  }

  public MissingRootCertificateException(String s) {
    super(s);
  }

  public MissingRootCertificateException(Throwable cause) {
    super(cause);
  }

}
