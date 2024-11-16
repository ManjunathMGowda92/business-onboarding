package org.fourstack.backoffice.service;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.fourstack.backoffice.config.BackOfficeKafkaPropData;
import org.fourstack.backoffice.entity.AgentInstitutionEntity;
import org.fourstack.backoffice.entity.AiOuMappingEntity;
import org.fourstack.backoffice.entity.KafkaMessage;
import org.fourstack.backoffice.entity.OperationUnitEntity;
import org.fourstack.backoffice.entity.config.TopicConfigurations;
import org.fourstack.backoffice.enums.EventType;
import org.fourstack.backoffice.enums.OperationStatus;
import org.fourstack.backoffice.mapper.EntityMapper;
import org.fourstack.backoffice.model.business.AiDetails;
import org.fourstack.backoffice.model.business.AiOuMappingDetails;
import org.fourstack.backoffice.model.business.MasterDataRequest;
import org.fourstack.backoffice.model.business.OuDetails;
import org.fourstack.backoffice.repository.KafkaMessageRepository;
import org.fourstack.backoffice.util.BackOfficeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor(onConstructor_ = @Lazy)
public class KafkaPublisherService {
    private static final Logger logger = LoggerFactory.getLogger(KafkaPublisherService.class);

    private final BackOfficeKafkaPropData kafkaPropertiesConfig;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final EntityMapper mapper;
    private final KafkaMessageRepository kafkaRepository;

    public void publishAiDetails(AgentInstitutionEntity entity) {
        AiDetails details = mapper.convertToAiDetails(entity);
        MasterDataRequest masterDataRequest = mapper.constructMasterDataRequest(List.of(details),
                Collections.emptyList(), Collections.emptyList());
        publishMasterData(masterDataRequest, details.getAiId());
    }

    public void publishOuDetails(OperationUnitEntity entity) {
        OuDetails details = mapper.convertToOuDetails(entity);
        MasterDataRequest masterDataRequest = mapper.constructMasterDataRequest(Collections.emptyList(),
                List.of(details), Collections.emptyList());
        publishMasterData(masterDataRequest, details.getOuId());
    }

    public void publishAiOuDetails(AiOuMappingEntity entity) {
        AiOuMappingDetails details = mapper.convertToAiOuDetails(entity);
        MasterDataRequest masterDataRequest = mapper.constructMasterDataRequest(Collections.emptyList(),
                Collections.emptyList(), List.of(details));
        publishMasterData(masterDataRequest, details.getAiId().concat(details.getOuId()));
    }

    private void publishMasterData(MasterDataRequest request, String key) {
        CompletableFuture.runAsync(() -> publishMessage(request, EventType.MASTER_DATA_PUBLISH, key))
                .handleAsync((response, exception) -> {
                    if (BackOfficeUtil.isNotNull(exception)) {
                        logger.error("Exception while publishing the MasterData : {}", exception.getMessage());
                    } else {
                        logger.info("MasterData published to Kafka Successfully");
                    }
                    return response;
                });
    }

    private void publishMessage(Object request, EventType eventType, String key) {
        TopicConfigurations topicConfig = getTopicConfiguration(eventType);
        if (BackOfficeUtil.isNotNull(topicConfig)) {
            String topicName = topicConfig.getTopicName();
            try {
                ProducerRecord<String, String> producerRecord = getProducerRecord(request, topicName, key);
                logger.info("Publishing Kafka message to topic  : {} - {}", topicName, producerRecord.value());
                CompletableFuture<SendResult<String, String>> future = kafkaTemplate.send(producerRecord);
                future.exceptionally(exception -> {
                    logger.error("Exception in publishing message to topic : {}, message: {}",
                            topicName, exception.getMessage());
                    constructAndSaveKafkaMessage(request, eventType, key, exception, topicName);
                    return null;
                }).thenAcceptAsync(result -> {
                    if (BackOfficeUtil.isNotNull(result)) {
                        RecordMetadata recordMetadata = result.getRecordMetadata();
                        String topic = recordMetadata.topic();
                        int partition = recordMetadata.partition();
                        long offset = recordMetadata.offset();
                        logger.info("Message published to kafka topic : {} - partition : {} - offset : {}", topic, partition, offset);
                        constructAndSaveKafkaMessage(request, eventType, key, recordMetadata);
                    }
                });
            } catch (Exception exception) {
                logger.info("Exception while publishing the message: {}", exception.getMessage());
            }
        }
    }

    private ProducerRecord<String, String> getProducerRecord(Object request, String topicName, String key) {
        String result = BackOfficeUtil.convertToString(request);
        return new ProducerRecord<>(topicName, key, result);
    }

    private TopicConfigurations getTopicConfiguration(EventType eventType) {
        Map<String, TopicConfigurations> topicDetails = kafkaPropertiesConfig.getTopicDetails();
        return topicDetails.get(eventType.name());
    }

    private void constructAndSaveKafkaMessage(Object request, EventType eventType, String key,
                                              RecordMetadata recordMetadata) {
        KafkaMessage kafkaMessage = constructKafkaMessage(recordMetadata.topic(), request,
                OperationStatus.SUCCESS.name(), key, eventType);
        kafkaMessage.setOffset(String.valueOf(recordMetadata.offset()));
        kafkaMessage.setPartition(String.valueOf(recordMetadata.partition()));
        kafkaRepository.save(kafkaMessage);
    }

    private void constructAndSaveKafkaMessage(Object request, EventType eventType, String key,
                                              Throwable exception, String topicName) {
        KafkaMessage kafkaMessage = constructKafkaMessage(topicName, request, OperationStatus.FAILURE.name(),
                key, eventType);
        kafkaMessage.setExceptionMessage(exception.getMessage());
        kafkaRepository.save(kafkaMessage);
    }

    private KafkaMessage constructKafkaMessage(String topicName, Object message, String status,
                                               String identifier, EventType eventType) {
        KafkaMessage kafkaMessage = new KafkaMessage();
        kafkaMessage.setId(UUID.randomUUID().toString());
        kafkaMessage.setTopicName(topicName);
        kafkaMessage.setIdentifier(identifier);
        kafkaMessage.setEventType(eventType.name());
        kafkaMessage.setMessageData(BackOfficeUtil.convertToString(message));
        kafkaMessage.setStatus(status);
        kafkaMessage.setDate(BackOfficeUtil.getCurrentDate());
        kafkaMessage.setTimestamp(BackOfficeUtil.getCurrentTimeStamp());
        return kafkaMessage;
    }
}
