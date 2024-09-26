package org.fourstack.business.exception;

import lombok.Getter;

@Getter
public class ValidationException extends RuntimeException{
    private final String errorCode;
    private final String errorMsg;
    private final String errorField;

    public ValidationException(String errorCode, String errorMsg, String errorField) {
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
        this.errorField = errorField;
    }

    public ValidationException(String message, String errorCode, String errorMsg, String errorField) {
        super(message);
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
        this.errorField = errorField;
    }

    public ValidationException(String message, Throwable cause, String errorCode, String errorMsg, String errorField) {
        super(message, cause);
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
        this.errorField = errorField;
    }
}
