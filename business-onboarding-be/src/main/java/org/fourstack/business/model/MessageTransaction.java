package org.fourstack.business.model;

import lombok.Data;
import org.fourstack.business.enums.EventType;
import org.fourstack.business.enums.TransactionType;
import org.springframework.http.HttpStatus;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
public class MessageTransaction implements Serializable {
    @Serial
    private static final long serialVersionUID = -601581831713213285L;

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
