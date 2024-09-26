package org.fourstack.business_mock_server.model;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class B2BId implements Serializable {
    @Serial
    private static final long serialVersionUID = -5753448415541431686L;
    protected String value;
    protected String privacyType;
    protected String reason;
    protected String description;
    protected BusinessIdentifier businessIdentifier;
    protected BankAccount bankAccount;
}
