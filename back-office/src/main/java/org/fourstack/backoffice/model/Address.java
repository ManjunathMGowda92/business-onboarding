package org.fourstack.backoffice.model;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class Address implements Serializable {
    @Serial
    private static final long serialVersionUID = 3586635578913668411L;
    private String type;
    private String addressLine1;
    private String addressLine2;
    private String city;
    private String state;
    private String country;
    private String pincode;
}
