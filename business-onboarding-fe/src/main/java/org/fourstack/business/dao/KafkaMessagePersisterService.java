package org.fourstack.business.dao;

import lombok.RequiredArgsConstructor;
import org.fourstack.business.entity.KafkaMessage;
import org.fourstack.business.entity.event.Message;
import org.fourstack.business.enums.OperationStatus;
import org.fourstack.business.utils.BusinessUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor(onConstructor_ = @Lazy)
public class KafkaMessagePersisterService {
    private static final Logger logger = LoggerFactory.getLogger(KafkaMessagePersisterService.class);
    private final KafkaMessageRepository messageRepository;


    public <R, A> void saveKafkaAuditMessage(String topicName, Message<R, A> message) {
        logger.info("Persisting the Kafka Audit Message for topic : {}, identifier: {}, eventType: {}",
                topicName, message.getIdentifierKey(), message.getEventType());
        KafkaMessage kafkaMessage = getKafkaMessage(topicName, message, OperationStatus.SUCCESS.name());
        messageRepository.save(kafkaMessage);
    }

    public <R, A> void saveKafkaAuditMessage(String topicName, Message<R, A> message, Throwable exception) {
        logger.info("Persisting the Kafka Audit Message for topic : {}, identifier: {}, eventType: {}",
                topicName, message.getIdentifierKey(), message.getEventType());
        KafkaMessage kafkaMessage = getKafkaMessage(topicName, message, OperationStatus.FAILURE.name(), exception);
        messageRepository.save(kafkaMessage);
    }

    private <R, A> KafkaMessage getKafkaMessage(String topicName, Message<R, A> message, String status) {
        return constructKafkaMessage(topicName, message, status);
    }

    private static <R, A> KafkaMessage constructKafkaMessage(String topicName, Message<R, A> message, String status) {
        KafkaMessage kafkaMessage = new KafkaMessage();
        kafkaMessage.setId(UUID.randomUUID().toString());
        kafkaMessage.setTopicName(topicName);
        kafkaMessage.setIdentifier(message.getIdentifierKey());
        kafkaMessage.setEventType(message.getEventType().name());
        kafkaMessage.setMessageData(BusinessUtil.convertToString(message));
        kafkaMessage.setStatus(status);
        kafkaMessage.setDate(BusinessUtil.getCurrentDate());
        kafkaMessage.setTimestamp(BusinessUtil.getCurrentTimeStamp());
        return kafkaMessage;
    }

    private <R, A> KafkaMessage getKafkaMessage(String topicName, Message<R, A> message, String status, Throwable exception) {
        KafkaMessage kafkaMessage = constructKafkaMessage(topicName, message, status);
        kafkaMessage.setExceptionMessage(exception.toString());
        return kafkaMessage;
    }
}
