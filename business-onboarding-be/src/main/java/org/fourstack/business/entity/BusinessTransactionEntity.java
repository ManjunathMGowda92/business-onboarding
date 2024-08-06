package org.fourstack.business.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serial;
import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
@Document(collection = "business_transaction")
public class BusinessTransactionEntity extends Entity implements Serializable {
    @Serial
    private static final long serialVersionUID = -5553928020165542868L;
    private BusinessEntity businessEntity;
}
