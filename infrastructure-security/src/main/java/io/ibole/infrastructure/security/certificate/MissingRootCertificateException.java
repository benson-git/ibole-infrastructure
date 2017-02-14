package io.ibole.infrastructure.security.certificate;

/*********************************************************************************************
 * .
 * 
 * 
 * <p>
 * 版权所有：(c)2016， 深圳市拓润计算机软件开发有限公司
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
