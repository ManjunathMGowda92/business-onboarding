package org.fourstack.backoffice.exception;

import lombok.Getter;

@Getter
public class ValidationException extends RuntimeException{
  private final String errorCode;
  private final String errorMessage;
  private final String errorField;

  public ValidationException(String errorCode, String errorMessage, String errorField) {
    this.errorCode = errorCode;
    this.errorMessage = errorMessage;
    this.errorField = errorField;
  }

  public ValidationException(String message, Throwable cause, String errorCode, String errorMessage, String errorField) {
    super(message, cause);
    this.errorCode = errorCode;
    this.errorMessage = errorMessage;
    this.errorField = errorField;
  }
}
