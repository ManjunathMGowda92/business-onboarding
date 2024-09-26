package org.fourstack.business.config;

import lombok.Data;
import org.fourstack.business.entity.config.KafkaConfig;
import org.fourstack.business.entity.config.TopicConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
@ConfigurationProperties(prefix = "kafka")
@Data
public class KafkaPropertiesConfig {
    private Map<String, KafkaConfig> configurations;
    private Map<String, TopicConfig> topicDetails = new HashMap<>();
}
