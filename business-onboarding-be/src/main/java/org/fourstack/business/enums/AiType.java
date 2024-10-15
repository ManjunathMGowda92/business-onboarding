package org.fourstack.business.enums;

import lombok.Getter;

@Getter
public enum AiType {
    PARTICIPATING("PARTICIPATING"),
    NON_PARTICIPATING("NON_PARTICIPATING");

    private final String value;

    AiType(String value) {
        this.value = value;
    }
}
