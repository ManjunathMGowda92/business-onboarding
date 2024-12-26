package org.fourstack.business.config;

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
