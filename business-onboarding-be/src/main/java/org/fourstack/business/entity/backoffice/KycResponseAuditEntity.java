package org.fourstack.business.entity.backoffice;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.fourstack.business.entity.Entity;
import org.fourstack.business.model.backoffice.kyc.KycBusinessResponse;

@EqualsAndHashCode(callSuper = true)
@Data
public class KycResponseAuditEntity extends Entity {
  private KycBusinessResponse response;
}
