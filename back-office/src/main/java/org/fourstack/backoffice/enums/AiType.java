package org.fourstack.backoffice.enums;

import lombok.Getter;

@Getter
public enum AiType {
    PARTICIPATING("PARTICIPATING"), NON_PARTICIPATING("NON PARTICIPATING");

    AiType(String type) {
        this.type = type;
    }

    private final String type;
}
