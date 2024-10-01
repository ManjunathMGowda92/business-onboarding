package org.fourstack.backoffice.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.mapping.Document;

@EqualsAndHashCode(callSuper = true)
@Data
@Document(collection = "ai_ou_entity")
public class AiOuMappingEntity extends Entity{
    private String aiId;
    private String aiName;
    private String ouId;
    private String ouName;
    private String description;
    private String webhookUrl;
}
