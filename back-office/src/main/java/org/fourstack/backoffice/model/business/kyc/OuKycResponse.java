package org.fourstack.backoffice.model.business.kyc;

import lombok.Data;
import org.fourstack.backoffice.enums.OperationStatus;

@Data
public class OuKycResponse {
    private String ouId;
    private OperationStatus result;
    private String reason;
}
