package org.fourstack.backoffice.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.mapping.Document;

@EqualsAndHashCode(callSuper = true)
@Data
@Document(collection = "ai_entity")
public class AiEntity extends Entity{
    private String id;
    private String name;
    private String description;
    private String subscriberId;
    private String webhookUrl;
    private String headQuarter;
}
