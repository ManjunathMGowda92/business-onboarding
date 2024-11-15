package org.fourstack.backoffice.entity.config;

import lombok.Data;

@Data
public class KafkaConfig {
    private String bootstrapServers;
    private String keySerializer;
    private String valueSerializer;
    private int retries;
    private int deliveryTimeOut;
    private int requestTimeOut;
}
