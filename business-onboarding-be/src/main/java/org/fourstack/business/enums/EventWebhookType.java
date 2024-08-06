package org.fourstack.business.enums;

public enum EventWebhookType {
    RESP_CREATE_BUSINESS("/api/v1/onboarding/business/respRegisterEntity"),
    RESP_ADD_B2B("/api/v1/onboarding/business/respRegisterId"),
    RESP_CHECK_ENTITY("/api/v1/onboarding/business/respCheckEntity"),
    RESP_SEARCH_ENTITY("/api/v1/onboarding/business/respSearchEntity");


    private final String endPoint;

    EventWebhookType(String endPoint) {
        this.endPoint = endPoint;
    }

    public String getEndPoint() {
        return endPoint;
    }
}
