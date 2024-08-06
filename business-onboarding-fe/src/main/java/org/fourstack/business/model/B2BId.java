package org.fourstack.business.model;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class B2BId implements Serializable {
    @Serial
    private static final long serialVersionUID = -5753448415541431686L;
    private String value;
    private String privacyType;
    private String reason;
    private String description;
    private BusinessIdentifier businessIdentifier;
}
