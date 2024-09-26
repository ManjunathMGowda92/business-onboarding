package org.fourstack.business.enums;

import lombok.Getter;

@Getter
public enum LeiType {
    SOLE_PROP("SOLE PROP"),
    COMPANY("COMPANY"),
    HUF("HUF"),
    TRUST("TRUST"),
    GOVERNMENT_AGENCY("GOVERNMENT AGENCY");

    private final String type;

    LeiType(String type) {
        this.type = type;
    }
}
