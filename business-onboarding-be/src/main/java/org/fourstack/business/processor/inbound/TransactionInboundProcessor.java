package org.fourstack.business.processor.inbound;

import org.fourstack.business.entity.event.Message;
import org.fourstack.business.enums.EventType;
import org.fourstack.business.model.MessageTransaction;
import org.fourstack.business.model.TransactionError;

public interface TransactionInboundProcessor {

    MessageTransaction executeProcess(Message<?, ?> message);

    void validateAndPersistTransaction(MessageTransaction transaction);

    MessageTransaction transform(Message<?, ?> message);

    void validate(MessageTransaction transaction);

    EventType getEventType();

    void handleTransactionErrors(MessageTransaction transaction, TransactionError txnError);
}
