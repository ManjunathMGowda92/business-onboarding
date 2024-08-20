package org.fourstack.business.model;

import lombok.Data;
import org.fourstack.business.enums.EventType;
import org.fourstack.business.enums.TransactionType;
import org.springframework.http.HttpStatus;

import java.util.List;

@Data
public class MessageTransaction {
    private final String transactionId;
    private final String messageId;
    private final TransactionType transactionType;
    private final EventType eventType;
    private final String transactionTimeStamp;

    private Object request;
    private Acknowledgement acknowledgement;
    private HttpStatus responseStatus;
    private String responseMessage;
    private List<TransactionError> txnErrors;
}
