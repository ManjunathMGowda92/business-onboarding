package org.fourstack.business.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.fourstack.business.model.BusinessIdentifier;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serial;
import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
@Document(collection = "business_identifiers")
public class BusinessIdentifierEntity extends Entity implements Serializable {
    @Serial
    private static final long serialVersionUID = -4338507499438243382L;
    private String aiId;
    private String orgId;
    private String businessRole;
    private BusinessIdentifier identifier;
}
