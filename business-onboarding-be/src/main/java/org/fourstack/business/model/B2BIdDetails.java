package org.fourstack.business.model;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class B2BIdDetails implements Serializable {

    @Serial
    private static final long serialVersionUID = 4755089205350026943L;
    private String value;
    private String privacyType;
    private String reason;
    private String description;
    private BusinessIdentifier businessIdentifier;
}
