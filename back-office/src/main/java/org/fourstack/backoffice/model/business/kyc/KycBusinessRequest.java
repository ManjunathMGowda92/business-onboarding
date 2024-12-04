package org.fourstack.backoffice.model.business.kyc;

import lombok.Data;

import java.util.List;

@Data
public class KycBusinessRequest {
    private String requestType;
    private Head head;
    private Transaction txn;
    private Institute institute;
    private EditInstitute editInstitute;
    private List<AdditionalInfo> additionalInfos;
}
