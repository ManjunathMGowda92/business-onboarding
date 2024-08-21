package org.fourstack.business.enums;

import lombok.Getter;

@Getter
public enum ErrorCodeScenario {
    INPUT_0001("INP0001", "Mandatory field missing"),
    INPUT_0002("INP0002", "Invalid field format"),
    TXN_0004("TXN0004", "Transaction time is beyond server time configured"),
    TXN_0005("TXN0005", "Mismatch between the date provided and system date"),
    BONB_0001("INP0003", "Invalid PAN format"),
    BONB_0002("INP0004", "Invalid PAN and Business Type Combination");

    ErrorCodeScenario(String errorCode, String errorMsg) {
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }
    private final String errorCode;
    private final String errorMsg;
}
