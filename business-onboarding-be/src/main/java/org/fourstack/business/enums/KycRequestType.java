package org.fourstack.business.enums;

public enum KycRequestType {
    CREATE_BUSINESS("NEW BUSINESS"),
    EDIT_BUSINESS("EDIT BUSINESS"),
    NEW_AI_MAPPING("MAP TO AI");

    KycRequestType(String requestType) {
        this.requestType = requestType;
    }

    private final String requestType;

    public String getRequestType() {
        return requestType;
    }
}
