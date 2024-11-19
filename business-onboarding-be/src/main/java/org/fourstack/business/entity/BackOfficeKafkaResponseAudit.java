package org.fourstack.business.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serial;
import java.io.Serializable;

@Data
@Document(collection = "back_office_kafka_response_audits")
public class BackOfficeKafkaResponseAudit implements Serializable {
    @Serial
    private static final long serialVersionUID = -7002430608090470117L;
    @Id
    private String id;
    private String topicName;
    private String key;
    private String eventType;
    private String kafkaMessage;
    private String requestReceivedTimeStamp;
    private String date;
}
