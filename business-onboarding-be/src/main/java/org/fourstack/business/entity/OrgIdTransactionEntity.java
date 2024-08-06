package org.fourstack.business.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serial;
import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
@Document(collection = "organization_transactions")
public class OrgIdTransactionEntity extends Entity implements Serializable {
    @Serial
    private static final long serialVersionUID = 3307681102730548801L;
    private OrgIdEntity orgIdEntity;
}
