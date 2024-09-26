package org.fourstack.business.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serial;
import java.io.Serializable;

@Data
@Document(collection = "kafka_audit_messages")
public class KafkaMessage implements Serializable {
    @Serial
    private static final long serialVersionUID = 5928246374905171886L;
    @Id
    private String id;
    private String topicName;
    private String identifier;
    private String eventType;
    private String messageData;
    private String timestamp;
    private String date;
    private String status;
    private String exceptionMessage;
}
