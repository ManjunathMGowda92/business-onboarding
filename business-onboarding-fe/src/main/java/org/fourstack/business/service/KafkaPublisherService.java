package org.fourstack.business.service;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.fourstack.business.config.KafkaPropertiesConfig;
import org.fourstack.business.dao.KafkaMessagePersisterService;
import org.fourstack.business.entity.event.Message;
import org.fourstack.business.entity.event.TopicConfig;
import org.fourstack.business.enums.EventType;
import org.fourstack.business.exceptions.InvalidInputException;
import org.fourstack.business.mapper.ResponseMapper;
import org.fourstack.business.model.Acknowledgement;
import org.fourstack.business.model.ActivateB2BRequest;
import org.fourstack.business.model.B2BIdRegisterRequest;
import org.fourstack.business.model.BusinessRegisterRequest;
import org.fourstack.business.model.CheckBusinessRequest;
import org.fourstack.business.model.CommonData;
import org.fourstack.business.model.EditB2BIdRequest;
import org.fourstack.business.model.SearchBusinessRequest;
import org.fourstack.business.utils.BusinessUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor(onConstructor_ = @Lazy)
public class KafkaPublisherService {
    private static final Logger logger = LoggerFactory.getLogger(KafkaPublisherService.class);
    private final ResponseMapper responseMapper;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final KafkaPropertiesConfig propertiesConfig;
    private final KafkaMessagePersisterService kafkaMessageService;

    public Acknowledgement publishBusiness(BusinessRegisterRequest request, String endPoint) {
        CommonData commonData = request.getCommonData();
        Message<BusinessRegisterRequest, Acknowledgement> message = responseMapper.constructMessage(request,
                EventType.REQ_CREATE_BUSINESS, commonData.getHead().getMsgId(), commonData.getTxn().getId(), endPoint);
        publishMessage(message);
        return message.getAck();
    }

    public Acknowledgement publishBusiness(B2BIdRegisterRequest request, String endPoint) {
        CommonData commonData = request.getCommonData();
        Message<B2BIdRegisterRequest, Acknowledgement> message = responseMapper.constructMessage(request,
                EventType.REQ_ADD_B2B, commonData.getHead().getMsgId(), commonData.getTxn().getId(), endPoint);
        publishMessage(message);
        return message.getAck();
    }

    public Acknowledgement publishBusiness(CheckBusinessRequest request, String endPoint) {
        CommonData commonData = request.getCommonData();
        Message<CheckBusinessRequest, Acknowledgement> message = responseMapper.constructMessage(request,
                EventType.REQ_CHECK_BUSINESS, commonData.getHead().getMsgId(), commonData.getTxn().getId(), endPoint);
        publishMessage(message);
        return message.getAck();
    }

    public Acknowledgement publishBusiness(SearchBusinessRequest request, String endPoint) {
        CommonData commonData = request.getCommonData();
        Message<SearchBusinessRequest, Acknowledgement> message = responseMapper.constructMessage(request,
                EventType.REQ_SEARCH_BUSINESS, commonData.getHead().getMsgId(), commonData.getTxn().getId(), endPoint);
        publishMessage(message);
        return message.getAck();
    }

    public Acknowledgement publishBusiness(EditB2BIdRequest request, String endPoint) {
        CommonData commonData = request.getCommonData();
        Message<EditB2BIdRequest, Acknowledgement> message = responseMapper.constructMessage(request, EventType.REQ_EDIT_B2B,
                commonData.getHead().getMsgId(), commonData.getTxn().getId(), endPoint);
        publishMessage(message);
        return message.getAck();
    }

    public Acknowledgement publishBusiness(ActivateB2BRequest request, String endPoint) {
        CommonData commonData = request.getCommonData();
        Message<ActivateB2BRequest, Acknowledgement> message = responseMapper.constructMessage(request, EventType.REQ_ACTIVATE_B2B,
                commonData.getHead().getMsgId(), commonData.getTxn().getId(), endPoint);
        publishMessage(message);
        return message.getAck();
    }


    private <R, A> void publishMessage(Message<R, A> message) {
        TopicConfig topicConfiguration = getTopicConfiguration(message.getEventType());
        if (BusinessUtil.isNotNull(topicConfiguration)) {
            String topicName = topicConfiguration.getTopicName();
            ProducerRecord<String, String> producerRecord = getProducerRecord(message, topicName);
            logger.info("Publishing message to kafka topic : {}", topicName);
            CompletableFuture<SendResult<String, String>> future = kafkaTemplate.send(producerRecord);
            future.whenCompleteAsync((result, exception) -> {
                if (BusinessUtil.isNull(exception)) {
                    logger.info("Message published to kafka topic : {}", topicName);
                    kafkaMessageService.saveKafkaAuditMessage(topicName, message);
                } else {
                    logger.error("Exception in publishing the message to topic : {}, message: {}",
                            topicName, exception.getMessage());
                    kafkaMessageService.saveKafkaAuditMessage(topicName, message, exception);
                }
            });
        } else {
            logger.error("No Topic is configured for the event - {}", message.getEventType());
            throw new InvalidInputException("No Topic configured for event: " + message.getEventType());
        }
    }

    private <R, A> ProducerRecord<String, String> getProducerRecord(Message<R, A> message, String topicName) {
        String result = BusinessUtil.convertToString(message);
        return new ProducerRecord<>(topicName, message.getIdentifierKey(), result);
    }

    private TopicConfig getTopicConfiguration(EventType eventType) {
        Map<String, TopicConfig> topicDetails = propertiesConfig.getTopicDetails();
        return topicDetails.get(eventType.name());
    }
}
