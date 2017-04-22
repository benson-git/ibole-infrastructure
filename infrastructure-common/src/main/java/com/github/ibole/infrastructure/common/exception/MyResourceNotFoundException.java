package com.github.ibole.infrastructure.common.exception;

public final class MyResourceNotFoundException extends RuntimeException {

  private static final long serialVersionUID = -4353389640098206278L;

  public MyResourceNotFoundException() {
    super();
  }

  public MyResourceNotFoundException(final String message, final Throwable cause) {
    super(message, cause);
  }

  public MyResourceNotFoundException(final String message) {
    super(message);
  }

  public MyResourceNotFoundException(final Throwable cause) {
    super(cause);
  }

}
