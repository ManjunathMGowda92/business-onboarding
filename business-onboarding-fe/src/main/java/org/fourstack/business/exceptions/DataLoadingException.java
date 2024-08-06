package org.fourstack.business.exceptions;

import lombok.Getter;

@Getter
public class DataLoadingException extends RuntimeException{
    private final String message;

    public DataLoadingException(String message) {
        this.message = message;
    }

    public DataLoadingException(String message, Throwable cause) {
        super(cause);
        this.message = message;
    }
}
