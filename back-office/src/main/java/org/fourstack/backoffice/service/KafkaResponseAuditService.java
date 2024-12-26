package org.fourstack.backoffice.service;

import lombok.AllArgsConstructor;
import org.fourstack.backoffice.entity.kyc.KafkaResponseFailureAudits;
import org.fourstack.backoffice.entity.kyc.KycResponseAudit;
import org.fourstack.backoffice.enums.KycRequestType;
import org.fourstack.backoffice.model.business.kyc.KycBusinessResponse;
import org.fourstack.backoffice.repository.KafkaResponseFailureAuditsRepository;
import org.fourstack.backoffice.repository.kyc.KycResponseAuditRepository;
import org.fourstack.backoffice.util.BackOfficeUtil;
import org.fourstack.backoffice.util.KeyGenerationUtil;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor(onConstructor_ = @Lazy)
public class KafkaResponseAuditService {
  private final KafkaResponseFailureAuditsRepository failureAuditRepository;
  private final KycResponseAuditRepository auditRepository;

  public void saveAudit(KafkaResponseFailureAudits audit) {
    failureAuditRepository.save(audit);
  }

  public void saveKafkaFailureAudits(String request, String requestType, String flowType, Throwable exception) {
    KafkaResponseFailureAudits kafkaFailureAudit = getKafkaFailureAudit(request, requestType, flowType, exception);
    saveAudit(kafkaFailureAudit);
  }

  private KafkaResponseFailureAudits getKafkaFailureAudit(String request, String requestType, String flowType, Throwable exception) {
    KafkaResponseFailureAudits audit = new KafkaResponseFailureAudits();
    audit.setRequest(request);
    audit.setRequestType(requestType);
    audit.setExceptionMsg(exception.getMessage());
    audit.setFlowType(flowType);
    audit.setTimestamp(BackOfficeUtil.getCurrentTimeStamp());
    String key = KeyGenerationUtil.generateKafkaFailureAuditKey(requestType);
    audit.setKey(key);
    return audit;
  }

  public void saveKafkaAudits(KycBusinessResponse response, String requestType) {
    KycResponseAudit kycResponseAudit = getKycResponseAudit(response, requestType);
    auditRepository.save(kycResponseAudit);
  }

  private KycResponseAudit getKycResponseAudit(KycBusinessResponse response, String requestType) {
    KycResponseAudit audit = new KycResponseAudit();
    String objectId = response.getObjectId();
    audit.setObjectId(objectId);
    audit.setTxnId(response.getTxnId());
    audit.setTimeStamp(BackOfficeUtil.getCurrentTimeStamp());
    audit.setKycStatus(response.getKycOuResponse());
    audit.setStatus(response.getKycStatus());
    audit.setErrorResponse(response.getErrorResponse());
    KycRequestType kycRequestType = BackOfficeUtil.getKycRequestType(requestType);
    String databaseKey = KeyGenerationUtil.generateKafkaResponseAuditKey(BackOfficeUtil.isNotNullOrEmpty(objectId) ? objectId : "",
            kycRequestType.name());
    audit.setKey(databaseKey);
    return audit;
  }
}
