package org.fourstack.business.exception;

import lombok.Getter;

@Getter
public class ObjectMappingException extends Exception {
    private final String message;
    private final String fieldName;

    public ObjectMappingException(String message, String fieldName) {
        this.message = message;
        this.fieldName = fieldName;
    }

    public ObjectMappingException(String message, String fieldName, Throwable cause) {
        super(cause);
        this.message = message;
        this.fieldName = fieldName;
    }
}
