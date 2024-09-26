package org.fourstack.business.model;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class BusinessIdentifier implements Serializable {
    @Serial
    private static final long serialVersionUID = 7457160616827601596L;
    private String documentName;
    private String value;
    private String registeredName;
}
