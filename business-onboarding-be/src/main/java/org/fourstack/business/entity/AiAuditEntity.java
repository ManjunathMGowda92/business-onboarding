package org.fourstack.business.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serial;
import java.io.Serializable;

@Data
@Document(collection = "ai_audit_entity")
public class AiAuditEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 5928246374905171886L;
    @Id
    private String key;
    private AiEntity entity;
}
