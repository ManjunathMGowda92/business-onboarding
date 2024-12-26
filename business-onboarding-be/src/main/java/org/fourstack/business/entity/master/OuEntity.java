package org.fourstack.business.entity.master;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.fourstack.business.entity.Entity;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serial;
import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
@Document(collection = "ou_entity")
public class OuEntity extends Entity implements Serializable {
    @Serial
    private static final long serialVersionUID = 5426600793271532199L;
    private String id;
    private String name;
}
