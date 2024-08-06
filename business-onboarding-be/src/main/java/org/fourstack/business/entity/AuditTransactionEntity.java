package org.fourstack.business.entity;

import lombok.Data;
import org.fourstack.business.enums.TransactionFlow;
import org.fourstack.business.model.MessageTransaction;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serial;
import java.io.Serializable;

@Data
@Document(collection = "transaction_audit")
public class AuditTransactionEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = -4012378539313824363L;
    @Id
    private String key;
    private String transactionId;
    private MessageTransaction transaction;
    private TransactionFlow flowType;

    private String createdTimeStamp;
}
