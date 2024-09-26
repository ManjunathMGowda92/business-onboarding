package org.fourstack.business.bootstrap;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.ListTopicsResult;
import org.apache.kafka.clients.admin.NewTopic;
import org.fourstack.business.config.KafkaPropertiesConfig;
import org.fourstack.business.entity.event.TopicConfig;
import org.fourstack.business.utils.BusinessUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Lazy;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor(onConstructor_ = @Lazy)
public class KafkaTopicsLoader implements CommandLineRunner {
    private static final Logger logger = LoggerFactory.getLogger(KafkaTopicsLoader.class);
    private final KafkaAdmin kafkaAdmin;
    private final KafkaPropertiesConfig kafkaPropertiesConfig;

    @Override
    public void run(String... args) throws Exception {
        logger.info("Configuring the topics....");
        try (AdminClient admin = AdminClient.create(kafkaAdmin.getConfigurationProperties())) {
            ListTopicsResult topics = admin.listTopics();
            Set<String> topicNames = topics.names().get();
            Map<String, TopicConfig> topicConfigurations = kafkaPropertiesConfig.getTopicDetails();

            List<NewTopic> newTopics = getTopicsForCreationOrModification(topicConfigurations, topicNames);
            admin.createTopics(newTopics);
        }
    }

    private List<NewTopic> getTopicsForCreationOrModification(Map<String, TopicConfig> topicConfigurations,
                                                              Set<String> existingTopicNames) {
        List<NewTopic> newTopics = new ArrayList<>();
        for (Map.Entry<String, TopicConfig> entry : topicConfigurations.entrySet()) {
            TopicConfig topicConfig = entry.getValue();
            String topicName = topicConfig.getTopicName();
            if (!existingTopicNames.contains(topicName) || topicConfig.isReAssignmentRequired()) {
                logger.info("Creation or Reassignment required. TopicName : {}, partitions - {}",
                        topicConfig.getTopicName(), topicConfig.getPartitionCount());
                NewTopic topic = BusinessUtil.createTopic(topicConfig.getTopicName(),
                        topicConfig.getPartitionCount(), topicConfig.getReplicaCount());
                newTopics.add(topic);
            } else {
                logger.info("Existing topic or Reassignment not required - Topic Name : {}, partitions - {}",
                        topicConfig.getTopicName(), topicConfig.getPartitionCount());
            }
        }
        return newTopics;
    }
}
