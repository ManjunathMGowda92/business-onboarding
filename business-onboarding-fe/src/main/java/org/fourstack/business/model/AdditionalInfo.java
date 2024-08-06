package org.fourstack.business.model;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class AdditionalInfo implements Serializable {
    @Serial
    private static final long serialVersionUID = 5614437983909766440L;

    private String name;
    private String value;
}
