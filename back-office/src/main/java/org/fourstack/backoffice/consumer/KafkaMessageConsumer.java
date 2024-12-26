package org.fourstack.backoffice.consumer;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.fourstack.backoffice.model.business.MasterDataResponse;
import org.fourstack.backoffice.processor.BusinessKycProcessor;
import org.fourstack.backoffice.util.BackOfficeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(onConstructor_ = @Lazy)
public class KafkaMessageConsumer {
  private static final Logger logger = LoggerFactory.getLogger(KafkaMessageConsumer.class);
  private final BusinessKycProcessor kycProcessor;

  @KafkaListener(topics = {"BUSINESS-KYC-REQUEST", "MASTER-DATA-RESPONSE"})
  public void consumeMessages(ConsumerRecord<?, ?> consumerRecord) {
    logger.info("KYC or Master Data Response received from Kafka");
    String topic = consumerRecord.topic();
    Object value = consumerRecord.value();
    if (value instanceof String str) {
      logger.info("Received data from Kafka-topic : {} - {}", topic, str);
      processMessage(str, topic);
    }
  }

  private void processMessage(String message, String topic) {
    if (topic.equals("MASTER-DATA-RESPONSE")) {
      processMasterDataResponse(message);
    } else if (topic.equals("BUSINESS-KYC-REQUEST")) {
      kycProcessor.processKyc(message);
    }
  }

  private void processMasterDataResponse(String message) {
    MasterDataResponse masterDataResponse = BackOfficeUtil.convertToObject(message, MasterDataResponse.class);
  }
}
