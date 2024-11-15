package org.fourstack.backoffice.exception;

import lombok.Getter;

@Getter
public class InvalidInputException extends RuntimeException {
    private final String errorMessage;

    public InvalidInputException(String errorMessage) {
        super(errorMessage);
        this.errorMessage = errorMessage;
    }
}
