package org.fourstack.backoffice.bootstrap;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.ListTopicsResult;
import org.apache.kafka.clients.admin.NewTopic;
import org.fourstack.backoffice.config.BackOfficeKafkaPropData;
import org.fourstack.backoffice.entity.config.TopicConfigurations;
import org.fourstack.backoffice.util.BackOfficeUtil;
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
import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor(onConstructor_ = @Lazy)
public class BackOfficeTopicsLoader implements CommandLineRunner {
    private static final Logger logger = LoggerFactory.getLogger(BackOfficeTopicsLoader.class);
    private final KafkaAdmin kafkaAdmin;
    private final BackOfficeKafkaPropData configData;
    @Override
    public void run(String... args) throws Exception {
        logger.info("Configuring the topic....");
        loadTopics();
    }

    private void loadTopics() throws ExecutionException, InterruptedException {
        try (AdminClient admin = AdminClient.create(kafkaAdmin.getConfigurationProperties())) {
            ListTopicsResult topics = admin.listTopics();
            Set<String> topicNames = topics.names().get();
            Map<String, TopicConfigurations> topicDetails = configData.getTopicDetails();
            List<NewTopic> newTopics = getTopicsForCreationOrModification(topicDetails, topicNames);
            admin.createTopics(newTopics);
        }
    }

    private List<NewTopic> getTopicsForCreationOrModification(Map<String, TopicConfigurations> topicDetails, Set<String> topicNames) {
        List<NewTopic> newTopics = new ArrayList<>();
        for (Map.Entry<String, TopicConfigurations> entry : topicDetails.entrySet()) {
            TopicConfigurations topicConfig = entry.getValue();
            String topicName = topicConfig.getTopicName();
            if (!topicNames.contains(topicName) || topicConfig.isReAssignmentRequired()) {
                logger.info("Creation or Reassignment required. TopicName : {}, partitions - {}",
                        topicConfig.getTopicName(), topicConfig.getPartitionCount());
                NewTopic topic = BackOfficeUtil.createTopic(topicConfig.getTopicName(),
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
