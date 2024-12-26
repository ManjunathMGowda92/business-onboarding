package org.fourstack.backoffice.entity.kyc;

import lombok.Data;
import org.fourstack.backoffice.model.business.kyc.AdditionalInfo;
import org.fourstack.backoffice.model.business.kyc.Head;
import org.fourstack.backoffice.model.business.kyc.Institute;
import org.fourstack.backoffice.model.business.kyc.OuKycResponse;
import org.fourstack.backoffice.model.business.kyc.Transaction;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Map;

@Data
@Document(collection = "kyc_request_audit")
public class KycRequestAuditEntity {
    @Id
    private String key;
    private String objectId;
    private String requestType;
    private Head head;
    private Transaction txn;
    private Institute institute;
    private Map<String, OuKycResponse> kycOuMap;
    private List<AdditionalInfo> additionalInfos;
    private String timeStamp;
}
