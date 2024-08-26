package org.fourstack.business.enums;

import lombok.Getter;

@Getter
public enum EventWebhookType {
    RESP_CREATE_BUSINESS("/api/v1/onboarding/business/respRegisterEntity"),
    RESP_ADD_B2B("/api/v1/onboarding/business/respRegisterId"),
    RESP_CHECK_ENTITY("/api/v1/onboarding/business/respCheckEntity"),
    RESP_SEARCH_ENTITY("/api/v1/onboarding/business/respSearchEntity"),
    REQ_EDIT_B2B("/v1/onboarding/business/respEditId"),
    REQ_ACTIVATE_DEACTIVATE_B2B("/v1/onboarding/business/respChangeIdStatus");


    private final String endPoint;

    EventWebhookType(String endPoint) {
        this.endPoint = endPoint;
    }
}
