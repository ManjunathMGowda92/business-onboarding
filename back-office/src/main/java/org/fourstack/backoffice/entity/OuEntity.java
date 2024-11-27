package org.fourstack.backoffice.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.mapping.Document;

@EqualsAndHashCode(callSuper = true)
@Data
@Document(collection = "ou_entity")
public class OuEntity extends Entity{
    private String id;
    private String name;
    private String description;
    private String ifscCode;
    private String businessVPA;
    private String branchLocation;
}
