package org.fourstack.business.service;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.fourstack.business.config.KafkaPropertiesConfig;
import org.fourstack.business.entity.config.TopicConfig;
import org.fourstack.business.enums.EventType;
import org.fourstack.business.enums.KycRequestType;
import org.fourstack.business.exception.ObjectMappingException;
import org.fourstack.business.model.BusinessRegisterRequest;
import org.fourstack.business.model.backoffice.kyc.BackOfficeKycRequest;
import org.fourstack.business.model.backoffice.kyc.KycCreateBusinessRequest;
import org.fourstack.business.utils.BusinessUtil;
import org.fourstack.business.utils.JsonUtilityHelper;
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
public class KafkaBackofficePublisher {
    private static final Logger logger = LoggerFactory.getLogger(KafkaBackofficePublisher.class);

    private final KafkaPropertiesConfig kafkaPropertiesConfig;
    private final KafkaTemplate<String, String> kafkaTemplate;

    public void publishKycForCreateBusiness(BusinessRegisterRequest request) {
        String requestType = KycRequestType.CREATE_BUSINESS.getRequestType();
        KycCreateBusinessRequest kycRequest = KycCreateBusinessRequest.builder()
                .head(request.getCommonData().getHead())
                .txn(request.getCommonData().getTxn())
                .institute(request.getInstitute())
                .requestType(requestType)
                .additionalInfos(request.getAdditionalInfoList())
                .build();
        BackOfficeKycRequest backOfficeRequest = constructKycRequest(kycRequest, requestType,
                EventType.REQ_KYC_PUBLISH);
        publishMessage(backOfficeRequest);
    }

    private void publishMessage(BackOfficeKycRequest request) {
        CompletableFuture.runAsync(() -> publishMessage(request.getRequest(), request.getEventType(), request.getKey()))
                .handleAsync((response, exception) -> {
                    if (BusinessUtil.isNotNull(exception)) {
                        logger.error("Exception while publishing the KYC message : {}", exception.getMessage());
                    } else {
                        logger.info("Message published to Kafka Successfully");
                    }
                    return response;
                });
    }

    private void publishMessage(Object request, EventType eventType, String key) {
        TopicConfig topicConfig = getTopicConfiguration(eventType);
        if (BusinessUtil.isNotNull(topicConfig)) {
            String topicName = topicConfig.getTopicName();
            try {
                ProducerRecord<String, String> producerRecord = getProducerRecord(request, topicName, key);
                logger.info("Publishing KYC message for create Business : {}", topicName);
                CompletableFuture<SendResult<String, String>> future = kafkaTemplate.send(producerRecord);
                future.handle((result, exception) -> {
                    if (BusinessUtil.isNotNull(exception)) {
                        logger.error("Exception in publishing KYC message to topic : {}, message: {}",
                                topicName, exception.getMessage());
                    }
                    return result;
                }).thenAcceptAsync(result -> {
                    if (BusinessUtil.isNotNull(result)) {
                        logger.info("Message published to KYC kafka topic : {}", topicName);
                    }
                });
            } catch (Exception exception) {
                logger.info("Exception while publishing the KYC message: {}", exception.getMessage());
            }
        }
    }

    private ProducerRecord<String, String> getProducerRecord(Object request, String topicName, String key)
            throws ObjectMappingException {
        String result = JsonUtilityHelper.convertToString(request);
        return new ProducerRecord<>(topicName, key, result);
    }

    private TopicConfig getTopicConfiguration(EventType eventType) {
        Map<String, TopicConfig> topicDetails = kafkaPropertiesConfig.getTopicDetails();
        return topicDetails.get(eventType.name());
    }

    private BackOfficeKycRequest constructKycRequest(Object request, String key, EventType eventType) {
        return BackOfficeKycRequest.builder()
                .request(request)
                .key(key)
                .eventType(eventType)
                .build();
    }
}
