package org.fourstack.business.enums;

import lombok.Getter;

@Getter
public enum EventWebhookType {
    REQ_CREATE_BUSINESS("/v1/onboarding/business/reqRegisterEntity"),
    REQ_ADD_B2B("/v1/onboarding/business/reqRegisterId"),
    REQ_CHECK_BUSINESS("/v1/onboarding/business/reqCheckEntity"),
    REQ_SEARCH_BUSINESS("/v1/onboarding/business/reqSearchEntity"),
    REQ_EDIT_B2B("/v1/onboarding/business/reqEditId"),
    REQ_ACTIVATE_DEACTIVATE_B2B("/v1/onboarding/business/reqChangeIdStatus");

    private final String endPoint;

    EventWebhookType(String endPoint) {
        this.endPoint = endPoint;
    }
}
