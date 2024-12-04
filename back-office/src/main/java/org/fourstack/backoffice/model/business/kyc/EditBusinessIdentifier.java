package org.fourstack.backoffice.model.business.kyc;

import lombok.Data;

@Data
public class EditBusinessIdentifier {
    private BusinessIdentifier currentIdentifier;
    private BusinessIdentifier newIdentifier;
}
