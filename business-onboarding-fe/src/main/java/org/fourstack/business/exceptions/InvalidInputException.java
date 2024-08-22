package org.fourstack.business.exceptions;

import lombok.Getter;

@Getter
public class InvalidInputException extends RuntimeException{
    private final String message;

    public InvalidInputException(String message) {
        this.message = message;
    }

    public InvalidInputException(String message, Throwable cause) {
        super(cause);
        this.message = message;
    }
}
