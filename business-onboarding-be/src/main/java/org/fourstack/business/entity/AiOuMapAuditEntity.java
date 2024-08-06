package org.fourstack.business.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serial;
import java.io.Serializable;

@Data
@Document(collection = "ai_ou_audit_entity")
public class AiOuMapAuditEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 7373200203728282317L;
    @Id
    private String key;
    private AiOuMapEntity entity;
}
