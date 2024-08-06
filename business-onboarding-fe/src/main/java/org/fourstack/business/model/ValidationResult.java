package org.fourstack.business.model;

import org.fourstack.business.enums.OperationStatus;

public record ValidationResult(OperationStatus status, String errorCode, String errorMessage, String fieldName) {
    public ValidationResult(OperationStatus status, String errorCode, String errorMessage, String fieldName) {
        this.status = status;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.fieldName = fieldName;
    }

    public boolean isSuccess() {
        return this.status == OperationStatus.SUCCESS;
    }

    public boolean isFailure() {
        return this.status == OperationStatus.FAILURE;
    }
}
