package org.fourstack.business.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serial;
import java.io.Serializable;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
@Document(collection = "search_identifier")
public class SearchIdentifier extends Entity implements Serializable {
    @Serial
    private static final long serialVersionUID = 2879567616108338534L;
    private String identifierType;
    private String identifierValue;
    private Set<String> businessIds;
}
