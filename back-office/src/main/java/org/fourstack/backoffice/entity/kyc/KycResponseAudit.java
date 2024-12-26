package org.fourstack.backoffice.entity.kyc;

import lombok.Data;
import org.fourstack.backoffice.enums.OperationStatus;
import org.fourstack.backoffice.model.ErrorResponse;
import org.fourstack.backoffice.model.business.kyc.OuKycResponse;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document(collection = "kyc_response")
public class KycResponseAudit {
    @Id
    private String key;
    private String objectId;
    private String txnId;
    private String timeStamp;
    private List<OuKycResponse> kycStatus;
    private OperationStatus status;
    private ErrorResponse errorResponse;
}
