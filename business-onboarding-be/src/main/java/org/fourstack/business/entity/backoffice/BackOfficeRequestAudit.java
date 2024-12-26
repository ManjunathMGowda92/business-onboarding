package org.fourstack.business.entity.backoffice;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serial;
import java.io.Serializable;

@Data
@Document(collection = "back_office_kafka_request_audits")
public class BackOfficeRequestAudit implements Serializable {
    @Serial
    private static final long serialVersionUID = 8292048820920600389L;
    @Id
    private String id;
    private String topicName;
    private String partition;
    private String offset;
    private String identifier;
    private String eventType;
    private String messageData;
    private String requestSentTimestamp;
    private String date;
    private String status;
    private String exceptionMessage;
}
