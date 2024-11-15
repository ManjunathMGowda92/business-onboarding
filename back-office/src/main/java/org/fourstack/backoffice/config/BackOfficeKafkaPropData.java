package org.fourstack.backoffice.config;

import lombok.Data;
import org.fourstack.backoffice.entity.config.KafkaConfig;
import org.fourstack.backoffice.entity.config.TopicConfigurations;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
@ConfigurationProperties(prefix = "kafka")
@Data
public class BackOfficeKafkaPropData {
    private Map<String, KafkaConfig> configurations = new HashMap<>();
    private Map<String, TopicConfigurations> topicDetails = new HashMap<>();

}
