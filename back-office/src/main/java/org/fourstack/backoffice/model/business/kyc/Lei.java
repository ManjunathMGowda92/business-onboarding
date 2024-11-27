package org.fourstack.backoffice.model.business.kyc;

import lombok.Data;

@Data
public class Lei {
    private String documentName;
    private String type;
    private String value;
    private String registeredName;
}
