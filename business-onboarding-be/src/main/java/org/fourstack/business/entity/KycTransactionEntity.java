package org.fourstack.business.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.fourstack.business.enums.KycRequestType;
import org.fourstack.business.model.KycStatusResponse;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

@EqualsAndHashCode(callSuper = true)
@Data
@Document(collection = "kyc_transaction")
public class KycTransactionEntity extends Entity implements Serializable {
    @Serial
    private static final long serialVersionUID = -4740639604617842173L;

    private String txnId;
    private KycRequestType requestType;
    private String objectId;
    private Map<String, List<KycStatusResponse>> kycStatusMap;
}
