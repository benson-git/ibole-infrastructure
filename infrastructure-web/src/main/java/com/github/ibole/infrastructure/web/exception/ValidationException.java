package com.github.ibole.infrastructure.web.exception;

/**
 * Thrown when validation conflict error is found. Message used to describe the validation error.
 */
public class ValidationException extends RuntimeException {

  private static final long serialVersionUID = -3223085135573583549L;

    public ValidationException(final String message) {
        super(message);
    }

}
