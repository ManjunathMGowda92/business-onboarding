package org.fourstack.backoffice.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serial;
import java.io.Serializable;

@Data
@Document(collection = "ou_audit_entity")
public class OuAuditEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1439798599331434884L;
    @Id
    private String key;
    private OperationUnitEntity entity;
}
