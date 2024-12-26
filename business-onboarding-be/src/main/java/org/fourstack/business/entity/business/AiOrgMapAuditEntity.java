package org.fourstack.business.entity.business;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.fourstack.business.entity.Entity;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serial;
import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
@Document(collection = "ai_org_mapping_audits")
public class AiOrgMapAuditEntity extends Entity implements Serializable {
  @Serial
  private static final long serialVersionUID = 3307681102730548801L;
  private AiOrgMapEntity entity;
}
