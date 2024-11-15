package org.fourstack.business.consumer;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.fourstack.business.dao.service.MasterDataService;
import org.fourstack.business.exception.ObjectMappingException;
import org.fourstack.business.model.backoffice.masterdata.AiDetails;
import org.fourstack.business.model.backoffice.masterdata.AiOuMappingDetails;
import org.fourstack.business.model.backoffice.masterdata.MasterDataRequest;
import org.fourstack.business.model.backoffice.masterdata.OuDetails;
import org.fourstack.business.utils.BusinessUtil;
import org.fourstack.business.utils.JsonUtilityHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor_ = @Lazy)
public class BackOfficeMessageConsumer {
    private static final Logger logger = LoggerFactory.getLogger(BackOfficeMessageConsumer.class);
    private final MasterDataService masterDataService;

    @KafkaListener(topics = {"KYC-RESPONSE", "MASTER-DATA"})
    public void consumeMessages(ConsumerRecord<?, ?> consumerRecord) {
        logger.info("Back-office message received from Kafka");
        String topic = consumerRecord.topic();
        Object value = consumerRecord.value();
        if (value instanceof String str) {
            logger.info("Consumed the request from topic : {} - {}", topic, str);
            processMessage(str, topic);
        }
    }

    private void processMessage(String message, String topic) {
        if (topic.equals("MASTER-DATA")) {
            processMasterData(message);
        } else {
            throw new IllegalStateException("Unexpected value: " + topic);
        }
    }

    private void processMasterData(String message) {
        try {
            MasterDataRequest masterDataRequest = JsonUtilityHelper.convertToObject(message, MasterDataRequest.class);
            createAiDetails(masterDataRequest);
            createOuDetails(masterDataRequest);
            createAiOuDetails(masterDataRequest);
        } catch (ObjectMappingException e) {
            throw new RuntimeException(e);
        }

    }

    private void createAiOuDetails(MasterDataRequest masterDataRequest) {
        if (BusinessUtil.isCollectionNotNullOrEmpty(masterDataRequest.getAiOuDetails())) {
            List<AiOuMappingDetails> aiOuDetails = masterDataRequest.getAiOuDetails();
            for (AiOuMappingDetails aiOuDetail : aiOuDetails) {
                masterDataService.saveEntity(aiOuDetail);
            }
        }
    }

    private void createOuDetails(MasterDataRequest masterDataRequest) {
        if (BusinessUtil.isCollectionNotNullOrEmpty(masterDataRequest.getOuDetails())) {
            List<OuDetails> ouDetails = masterDataRequest.getOuDetails();
            for (OuDetails ouDetail : ouDetails) {
                masterDataService.saveEntity(ouDetail);
            }
        }
    }

    private void createAiDetails(MasterDataRequest masterDataRequest) {
        if (BusinessUtil.isCollectionNotNullOrEmpty(masterDataRequest.getAiDetails())) {
            List<AiDetails> aiDetails = masterDataRequest.getAiDetails();
            for (AiDetails aiDetail : aiDetails) {
                masterDataService.saveEntity(aiDetail);
            }
        }
    }
}
