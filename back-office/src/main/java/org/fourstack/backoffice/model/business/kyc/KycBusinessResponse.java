package org.fourstack.backoffice.model.business.kyc;

import lombok.Data;

import java.util.List;

@Data
public class KycBusinessResponse {
    private String txnId;
    private String timeStamp;
    private String objectId;
    private List<OuKycResponse> kycStatus;
}
