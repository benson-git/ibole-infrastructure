package com.github.ibole.infrastructure.web.exception;

public class ErrorMessage {

    private int code;
    private String message;
    private String devMessage;
    private ValidationErrorDTO validationError;

  public ErrorMessage(final int status, final String message, final String devMessage,
      ValidationErrorDTO validationError) {
    super();

    this.code = status;
    this.message = message;
    this.devMessage = devMessage;
    this.validationError = validationError;
  }

    //

    public int getCode() {
        return code;
    }

    public void setCode(final int status) {
        this.code = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(final String message) {
        this.message = message;
    }

    public String getDevMessage() {
        return devMessage;
    }

    public void setDevMessage(final String developerMessage) {
        this.devMessage = developerMessage;
    }

    //

    /**
     * @return the validationError
     */
    public ValidationErrorDTO getValidationError() {
      return validationError;
    }

    /**
     * @param validationError the validationError to set
     */
    public void setValidationError(ValidationErrorDTO validationError) {
      this.validationError = validationError;
    }

  @Override
  public final String toString() {
    final StringBuilder builder = new StringBuilder();
    builder.append("ErrorMessage [code=").append(code).append(", message=").append(message)
        .append(", devMessage=").append(devMessage).append(", validationError=")
        .append(validationError).append("]");
    return builder.toString();
  }

}
