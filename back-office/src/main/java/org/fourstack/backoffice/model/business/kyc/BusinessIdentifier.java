package org.fourstack.backoffice.model.business.kyc;

import lombok.Data;

@Data
public class BusinessIdentifier {
    private String documentName;
    private String value;
    private String registeredName;
}
