package org.fourstack.business.model;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class ContactNumber implements Serializable {
    @Serial
    private static final long serialVersionUID = -4276807001957570407L;
    private String type;
    private String countryCode;
    private String phoneNumber;
}
