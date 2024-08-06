package org.fourstack.business.entity.event;

import lombok.Data;

@Data
public class KafkaConfig {
    private String bootstrapServers;
    private String keySerializer;
    private String valueSerializer;
    private int retries;
}
