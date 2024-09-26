package org.fourstack.business.model;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class Tag implements Serializable {
    @Serial
    private static final long serialVersionUID = 7282856887345123108L;
    private String name;
    private String value;
}
