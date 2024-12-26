package org.fourstack.backoffice.model.business.kyc;

import lombok.Data;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Data
public class KycBusinessRequest {
    private String requestType;
    private Head head;
    private Transaction txn;
    private Institute institute;
    private EditInstitute editInstitute;
    private List<AdditionalInfo> additionalInfos;
    private Set<String> kycRequestedOuIds;
    private Map<String, Boolean> ouExistenceMap;
}
