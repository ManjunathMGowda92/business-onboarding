package org.fourstack.business.enums;

public enum BusinessRole {
    SUPPLIER_AND_BUYER("Supplier and Buyer"),
    BUYER("Buyer");

    private final String role;

    BusinessRole(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }
}
