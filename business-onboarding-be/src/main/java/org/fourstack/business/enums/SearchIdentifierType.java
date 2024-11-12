package org.fourstack.business.enums;

import lombok.Getter;

@Getter
public enum SearchIdentifierType {
    CONTACT_NUMBER("CONTACT NUMBER"),
    EMAIL("EMAIL"),
    NAME("NAME");

    private final String searchType;

    SearchIdentifierType(String searchType) {
        this.searchType = searchType;
    }
}
