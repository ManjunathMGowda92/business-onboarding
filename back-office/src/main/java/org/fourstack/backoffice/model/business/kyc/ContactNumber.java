package org.fourstack.backoffice.model.business.kyc;

import lombok.Data;

@Data
public class ContactNumber {
    private String type;
    private String countryCode;
    private String phoneNumber;
}
