package org.fourstack.business.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
@ConfigurationProperties(prefix = "back-office-kafka")
@Data
public class KafkaBackOfficePropertiesConfig {
    private Map<String, KafkaConfig> configurations;
    private Map<String, TopicConfig> topicDetails = new HashMap<>();
}
