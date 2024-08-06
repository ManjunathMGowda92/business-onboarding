package org.fourstack.business.exception;

import lombok.Getter;

@Getter
public class InvalidInputException extends RuntimeException {
    private final String exceptionMsg;
    private final String errorCode;
    private final String errorMessage;

    public InvalidInputException(String exceptionMsg, String errorCode, String errorMessage) {
        super(exceptionMsg);
        this.exceptionMsg = exceptionMsg;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public InvalidInputException(String exceptionMsg, Throwable cause, String errorCode, String errorMessage) {
        super(exceptionMsg, cause);
        this.exceptionMsg = exceptionMsg;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

}
