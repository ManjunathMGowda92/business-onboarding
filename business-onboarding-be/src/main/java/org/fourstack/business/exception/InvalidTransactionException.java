package org.fourstack.business.exception;

import lombok.Getter;
import org.fourstack.business.enums.ErrorScenarioCode;

@Getter
public class InvalidTransactionException extends RuntimeException{
    private final String message;
    private final String fieldName;
    private final ErrorScenarioCode scenarioCode;

    public InvalidTransactionException(String message, String fieldName, ErrorScenarioCode scenarioCode) {
        this.message = message;
        this.fieldName = fieldName;
        this.scenarioCode = scenarioCode;
    }
}
