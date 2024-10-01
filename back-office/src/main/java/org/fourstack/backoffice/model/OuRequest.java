package org.fourstack.backoffice.model;

import lombok.Data;

@Data
public class OuRequest {
    protected String ouId;
    protected String operationUnitName;
    protected String operationUnitAliasName;
    protected String description;
    protected String mailId;
    protected Address registeredAddress;
    protected Address communicationAddress;
    protected BankDetails bankDetails;
}
