package org.fourstack.backoffice.entity.config;

import lombok.Data;

@Data
public class TopicConfigurations {
    private String topicName;
    private int partitionCount;
    private int replicaCount;
    private boolean reAssignmentRequired;
}
