package org.fourstack.backoffice.entity.kyc;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "failure_audits")
public class KafkaResponseFailureAudits {
  @Id
  private String key;
  private String request;
  private String requestType;
  private String flowType;
  private String timestamp;
  private String exceptionMsg;
}
