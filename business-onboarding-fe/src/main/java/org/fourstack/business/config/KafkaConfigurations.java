package org.fourstack.business.config;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.config.TopicConfig;
import org.fourstack.business.entity.event.KafkaConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;
import java.util.Map;

@Configuration
@RequiredArgsConstructor(onConstructor_ = @Lazy)
public class KafkaConfigurations {
    private final KafkaPropertiesConfig configs;

    @Bean
    public KafkaAdmin kafkaAdmin() {
        return new KafkaAdmin(producerConfig());
    }

    @Bean
    public Map<String, Object> producerConfig() {
        Map<String, KafkaConfig> configMap = configs.getConfigurations();
        KafkaConfig producer = configMap.get("producer");
        Map<String, Object> producerConfigs = new HashMap<>();
        producerConfigs.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, producer.getBootstrapServers());
        producerConfigs.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, producer.getKeySerializer());
        producerConfigs.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, producer.getValueSerializer());
        producerConfigs.put(ProducerConfig.RETRIES_CONFIG, producer.getRetries());
        return producerConfigs;
    }

    @Bean
    public ProducerFactory<String, String> producerFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfig());
    }

    @Bean
    public KafkaTemplate<String, String> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }
}
