package org.fourstack.backoffice.model.business.kyc;

import lombok.Data;
import org.fourstack.backoffice.enums.OperationStatus;
import org.fourstack.backoffice.model.ErrorResponse;

import java.util.List;

@Data
public class KycBusinessResponse {
    private String txnId;
    private String timeStamp;
    private String objectId;
    private List<OuKycResponse> kycOuResponse;
    private OperationStatus kycStatus;
    private ErrorResponse errorResponse;
}
