package org.fourstack.business.model;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class Lei implements Serializable {
    @Serial
    private static final long serialVersionUID = 4947739446108103328L;
    private String documentName;
    private String type;
    private String value;
    private String registeredName;
}
