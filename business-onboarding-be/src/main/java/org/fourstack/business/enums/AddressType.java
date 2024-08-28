package org.fourstack.business.enums;

import lombok.Getter;

@Getter
public enum AddressType {
    REGISTERED("REGISTERED"),
    CORPORATE("CORPORATE"),
    RESIDENTIAL("RESIDENTIAL"),
    HEAD_OFFICE("HEAD OFFICE"),

    BRANCH_OFFICE("BRANCH OFFICE"),
    OTHER("OTHER");

    private final String type;

    AddressType(String type) {
        this.type = type;
    }
}
