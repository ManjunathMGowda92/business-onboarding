package org.fourstack.business.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serial;
import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
@Document(collection = "ai_ou_mapping")
public class AiOuMapEntity extends Entity implements Serializable {
    @Serial
    private static final long serialVersionUID = -1106020156574680848L;
    private String aiId;
    private String ouId;
    private String webhookUrl;
}
