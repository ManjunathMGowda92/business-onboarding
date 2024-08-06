package org.fourstack.business.processor;

import org.fourstack.business.entity.event.Message;
import org.fourstack.business.model.MessageTransaction;
import org.springframework.http.HttpStatus;

public interface MessageProcessor {

    default void processMessage(Message<?, ?> message) {
        MessageTransaction transaction = transformAndValidate(message);
        if (HttpStatus.OK == transaction.getResponseStatus()) {
            executeBusinessTransactions(transaction);
        }
        sendOutboundRequest(transaction);
    }

    MessageTransaction transformAndValidate(Message<?, ?> message);

    void executeBusinessTransactions(MessageTransaction transaction);

    void sendOutboundRequest(MessageTransaction transaction);
}
