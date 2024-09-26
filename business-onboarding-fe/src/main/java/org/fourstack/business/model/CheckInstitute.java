package org.fourstack.business.model;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class CheckInstitute implements Serializable {
    @Serial
    private static final long serialVersionUID = -6378270628614739942L;
    private String documentName;
    private String type;
    private String value;
    private String registeredName;
}
