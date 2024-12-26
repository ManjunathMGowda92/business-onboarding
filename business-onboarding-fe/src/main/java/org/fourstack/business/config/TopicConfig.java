package org.fourstack.business.config;

import lombok.Data;

@Data
public class TopicConfig {
    private String topicName;
    private int partitionCount;
    private int replicaCount;
    private boolean reAssignmentRequired;
}
