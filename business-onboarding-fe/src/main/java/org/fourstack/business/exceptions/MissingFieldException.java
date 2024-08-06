package org.fourstack.business.exceptions;

import lombok.Getter;

@Getter
public class MissingFieldException extends RuntimeException{
    private final String message;
    private final String fieldName;

    public MissingFieldException(String message, String fieldName) {
        this.message = message;
        this.fieldName = fieldName;
    }
}
