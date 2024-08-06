package org.fourstack.business.enums;

import lombok.Getter;

@Getter
public enum ErrorCodeScenario {
    INPUT_0001("INP0001", "Mandatory field missing"),
    INPUT_0002("INP0002", "Invalid field format");

    ErrorCodeScenario(String errorCode, String errorMsg) {
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }
    private final String errorCode;
    private final String errorMsg;
}
