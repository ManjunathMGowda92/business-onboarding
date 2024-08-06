package org.fourstack.business.entity;

import lombok.Data;
import org.fourstack.business.enums.TransactionStatus;
import org.fourstack.business.enums.TransactionSubStatus;
import org.fourstack.business.enums.TransactionType;
import org.fourstack.business.model.TransactionError;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serial;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@Document(collection = "transaction")
public class TransactionEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = -2896476844341449259L;
    @Id
    private String key;
    private final String transactionId;
    private final TransactionType type;
    private TransactionStatus status;
    private TransactionSubStatus subStatus;
    private Map<String, TransactionError> txnErrors;

    private String createdTimeStamp;
    private String lastModifiedTimeStamp;
}
