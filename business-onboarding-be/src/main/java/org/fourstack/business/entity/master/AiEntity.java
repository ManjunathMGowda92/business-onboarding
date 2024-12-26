package org.fourstack.business.entity.master;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.fourstack.business.entity.Entity;
import org.fourstack.business.enums.AiType;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serial;
import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
@Document(collection = "ai_entity")
public class AiEntity extends Entity implements Serializable {
    @Serial
    private static final long serialVersionUID = 3756503135108981107L;
    private String id;
    private String name;
    private String subscriberId;
    private AiType aiType;
}
