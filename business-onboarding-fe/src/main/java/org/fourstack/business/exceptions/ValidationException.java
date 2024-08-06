package org.fourstack.business.exceptions;

import lombok.Getter;
import org.fourstack.business.utils.BusinessUtil;

@Getter
public class ValidationException extends RuntimeException {
    private final String message;
    private final String errorCode;
    private final String errorMessage;
    private final String fieldName;

    public ValidationException(String message, String errorCode, String errorMessage, String fieldName) {
        this.message = message;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.fieldName = fieldName;
    }

    public String getErrorResponse() {
        StringBuilder builder = new StringBuilder("[");
        builder.append("errorCode: ").append(errorCode)
                .append(", errorMessage: ").append(errorMessage)
                .append(", fieldName: ").append(fieldName)
                .append("]");
        return builder.toString();
    }
}
