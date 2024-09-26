package org.fourstack.business.exception;

import lombok.Getter;

@Getter
public class HttpFailureException extends RuntimeException{
    private final String message;

    public HttpFailureException(String message) {
        this.message = message;
    }
}
