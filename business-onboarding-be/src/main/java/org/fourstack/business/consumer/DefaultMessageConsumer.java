package org.fourstack.business.consumer;

import org.fourstack.business.entity.event.Message;
import org.fourstack.business.enums.EventType;
import org.fourstack.business.exception.ObjectMappingException;
import org.fourstack.business.processor.MessageProcessor;
import org.fourstack.business.utils.JsonUtilityHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class DefaultMessageConsumer {
    private static final Logger logger = LoggerFactory.getLogger(DefaultMessageConsumer.class);
    private final MessageProcessor businessMessageProcessor;
    private final MessageProcessor b2bIdMessageProcessor;
    private final MessageProcessor checkBusinessProcessor;
    private final MessageProcessor searchBusinessProcessor;

    public DefaultMessageConsumer(@Qualifier("businessMessageProcessor") MessageProcessor businessMessageProcessor,
                                  @Qualifier("b2bIdMessageProcessor") MessageProcessor b2bIdMessageProcessor,
                                  @Qualifier("checkBusinessMessageProcessor") MessageProcessor checkBusinessProcessor,
                                  @Qualifier("searchBusinessProcessor") MessageProcessor searchBusinessProcessor) {
        this.businessMessageProcessor = businessMessageProcessor;
        this.b2bIdMessageProcessor = b2bIdMessageProcessor;
        this.checkBusinessProcessor = checkBusinessProcessor;
        this.searchBusinessProcessor = searchBusinessProcessor;
    }

    @KafkaListener(topics = {"BUSINESS-REQUEST", "B2B-CREATE-REQUEST", "CHECK-BUSINESS-REQUEST"})
    public void consumeMessages(String message) {
        logger.info("{} - Message received from Kafka - {}", this.getClass().getSimpleName(), message);
        Message<?, ?> messageObj = constructMessage(message);
        processMessage(messageObj);
    }

    private Message<?, ?> constructMessage(String message) {
        try {
            Message<?, ?> messageObj = constructBaseMessage(message);
            updateRequestObject(messageObj);
            updateAckObject(messageObj);
            logger.info("Message converted to Business Object.");
            return messageObj;
        } catch (Exception e) {
            logger.error("Exception while converting the object : {}", e.getMessage());
            return new Message<>();
        }
    }

    private void updateAckObject(Message<?, ?> messageObj) {
        Object ack = JsonUtilityHelper.convertValue(messageObj.getAck(), messageObj.getAckObjType());
        messageObj.setAck(ack);
    }

    private void updateRequestObject(Message<?, ?> messageObj) {
        Object request = JsonUtilityHelper.convertValue(messageObj.getRequest(), messageObj.getRequestObjType());
        messageObj.setRequest(request);
    }

    private Message<?, ?> constructBaseMessage(String message) throws ObjectMappingException {
        return JsonUtilityHelper.convertToObject(message, Message.class);
    }

    private void processMessage(Message<?, ?> message) {
        EventType eventType = message.getEventType();
        switch (eventType) {
            case REQ_CREATE_BUSINESS -> businessMessageProcessor.processMessage(message);
            case REQ_ADD_B2B -> b2bIdMessageProcessor.processMessage(message);
            case REQ_CHECK_BUSINESS -> checkBusinessProcessor.processMessage(message);
            case REQ_SEARCH_BUSINESS -> searchBusinessProcessor.processMessage(message);
            default -> logger.error("{} - Unknown Event type received for message - {}",
                    this.getClass().getSimpleName(), eventType);
        }
    }
}
