package org.fourstack.backoffice.exception;

import lombok.Getter;

@Getter
public class InvalidInputException extends RuntimeException {
  private final String errorMessage;
  private final String errorCode;

  public InvalidInputException(String errorMessage, String errorCode) {
    super(errorMessage);
    this.errorMessage = errorMessage;
    this.errorCode = errorCode;
  }

    public InvalidInputException(Throwable cause, String errorMessage, String errorCode) {
        super(cause);
        this.errorMessage = errorMessage;
        this.errorCode = errorCode;
    }
}
