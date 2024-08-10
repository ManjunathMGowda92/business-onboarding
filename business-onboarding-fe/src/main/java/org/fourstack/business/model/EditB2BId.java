package org.fourstack.business.model;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class EditB2BId implements Serializable {
    @Serial
    private static final long serialVersionUID = 6015701694404746589L;
    private String value;
    private String action;
    private String privacyType;
    private String reason;
    private String description;
    private String primaryAi;
    private BusinessIdentifier businessIdentifier;
}
