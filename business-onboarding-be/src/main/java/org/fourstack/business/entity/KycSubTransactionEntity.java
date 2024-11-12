package org.fourstack.business.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serial;
import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
@Document(collection = "kyc_sub_transaction")
public class KycSubTransactionEntity extends Entity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1606769964425814711L;
    private String mainTxnId;
    private String kycTxnId;
    private String objectId;
    private String aiId;
    private String ouId;
    private String requestType;
    private String kycRequestTime;
    private String kycResponseTime;

}
