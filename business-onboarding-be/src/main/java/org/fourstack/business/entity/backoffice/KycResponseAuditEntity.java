package org.fourstack.business.entity.backoffice;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.fourstack.business.entity.Entity;
import org.fourstack.business.model.backoffice.kyc.KycBusinessResponse;
import org.springframework.data.mongodb.core.mapping.Document;

@EqualsAndHashCode(callSuper = true)
@Data
@Document(collection = "kyc_response_audits")
public class KycResponseAuditEntity extends Entity {
  private KycBusinessResponse response;
}
