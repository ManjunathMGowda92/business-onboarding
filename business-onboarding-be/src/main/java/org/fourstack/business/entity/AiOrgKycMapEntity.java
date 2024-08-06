package org.fourstack.business.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.fourstack.business.enums.EntityStatus;
import org.fourstack.business.model.KycOuStatus;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Document(collection = "kyc_org_ai_mapping")
public class AiOrgKycMapEntity extends Entity implements Serializable {
    @Serial
    private static final long serialVersionUID = -6425560926457654503L;
    private String aiId;
    private String txnId;
    private String orgId;
    private List<KycOuStatus> kycOuStatusList;
}
