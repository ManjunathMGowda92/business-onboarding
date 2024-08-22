package org.fourstack.business_mock_server.exceptions;

import lombok.Getter;

@Getter
public class InvalidInputException extends RuntimeException{
    private final String message;

    public InvalidInputException(String message) {
        this.message = message;
    }

    public InvalidInputException(Throwable cause, String message) {
        super(cause);
        this.message = message;
    }
}
