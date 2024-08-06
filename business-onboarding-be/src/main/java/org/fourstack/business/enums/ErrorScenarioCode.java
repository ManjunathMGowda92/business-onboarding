package org.fourstack.business.enums;

import lombok.Getter;

@Getter
public enum ErrorScenarioCode {
    HTTP_4XX("HTTP_4XX", "Http 4XX Error while sending outbound request"),
    HTTP_5XX("HTTP_5XX", "Http 5XX Error while sending outbound request"),
    GEN_0000("GEN0000", "Invalid transaction"),
    GEN_0001("GEN0001", "Invalid AI Id"),
    GEN_0002("GEN0002", "AI is Inactive"),
    GEN_0003("GEN0003", "Invalid OU Id"),
    GEN_0004("GEN0004", "OU is Inactive"),
    GEN_0005("GEN0005", "AI-OU Combination not exist"),
    GEN_0006("GEN0006", "AI-OU Combination is Inactive"),
    GEN_0007("GEN0007", "Invalid message consumed in Consumer"),

    TXN_0001("TXN0001", "Invalid transaction Id"),
    TXN_0002("TXN0002", "Duplicate transaction Id"),
    TXN_0003("TXN0003", "Transaction is already in completed state"),
    TXN_0004("TXN0004", "Transaction time is beyond server time configured"),

    BU_ONB_0001("ONB0001", "Business Entity already Exist and multiple " +
            "Business not allowed for lei value and type combination"),
    BU_ONB_0002("ONB0002", "Business Organization already exist"),
    BU_ONB_0003("ONB0003", "Business already exist for the objectId."),
    BU_ONB_0004("ONB0004", "B2B Id already exist"),
    BU_ONB_0005("ONB0005", "Business Identifier already exist"),
    BU_ONB_0006("ONB0006", "Requester B2B Id not exist"),
    BU_ONB_0007("ONB0007", "Requester B2B Id not active"),
    BU_ONB_0008("ONB0008", "Requested B2B Id value already exist"),
    BU_ONB_0009("ONB0009", "Business Identifier for requested B2B Id not exist"),
    BU_ONB_0010("ONB0010", "Business Identifier for requested B2B Id not associated to Organization of Requester B2BId"),
    BU_ONB_0011("ONB0011", "Requester B2B Id not associated to originated AI"),
    BU_ONB_0012("ONB0012", "Organization not exist for Requester B2B Id"),
    BU_ONB_0013("ONB0013", "Organization associated to Requester B2B Id is not active"),
    BU_ONB_0014("ONB0014", "Unknown Search Parameter"),
    BU_ONB_0015("ONB0015", "Private B2B Id is not allowed as search parameter"),
    BU_ONB_0016("ONB0016", "Business not found");


    private final String errorCode;
    private final String errorMsg;

    ErrorScenarioCode(String errorCode, String errorMsg) {
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }
}
