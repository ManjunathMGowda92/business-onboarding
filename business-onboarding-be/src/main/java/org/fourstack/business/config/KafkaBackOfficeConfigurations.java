package org.fourstack.business.config;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafka
@RequiredArgsConstructor(onConstructor_ = @Lazy)
public class KafkaBackOfficeConfigurations {
    private final KafkaBackOfficePropertiesConfig configs;

    @Bean(name = "backOfficeKafkaAdmin")
    public KafkaAdmin backOfficeKafkaAdmin() {
        return new KafkaAdmin(backOfficeProducerConfig());
    }

    @Bean
    public Map<String, Object> backOfficeProducerConfig() {
        Map<String, KafkaConfig> configurations = configs.getConfigurations();
        KafkaConfig producer = configurations.get("producer");
        Map<String, Object> producerConfigs = new HashMap<>();
        producerConfigs.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, producer.getBootstrapServers());
        producerConfigs.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, producer.getKeySerializer());
        producerConfigs.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, producer.getValueSerializer());
        producerConfigs.put(ProducerConfig.RETRIES_CONFIG, producer.getRetries());
        producerConfigs.put(ProducerConfig.DELIVERY_TIMEOUT_MS_CONFIG, producer.getDeliveryTimeOut());
        producerConfigs.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, producer.getRequestTimeOut());
        return producerConfigs;
    }

    @Bean
    public ProducerFactory<String, String> backOfficeProducerFactory() {
        return new DefaultKafkaProducerFactory<>(backOfficeProducerConfig());
    }

    @Bean(name = "backOfficeKafkaTemplate")
    public KafkaTemplate<String, String> backOfficeKafkaTemplate() {
        return new KafkaTemplate<>(backOfficeProducerFactory());
    }
}
