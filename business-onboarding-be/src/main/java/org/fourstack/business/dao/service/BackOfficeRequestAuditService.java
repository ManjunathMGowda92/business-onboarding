package org.fourstack.business.dao.service;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.fourstack.business.dao.repository.BackOfficeRequestAuditRepository;
import org.fourstack.business.entity.BackOfficeRequestAudit;
import org.fourstack.business.enums.EventType;
import org.fourstack.business.enums.OperationStatus;
import org.fourstack.business.exception.ObjectMappingException;
import org.fourstack.business.utils.BusinessUtil;
import org.fourstack.business.utils.JsonUtilityHelper;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(onConstructor_ = @Lazy)
public class BackOfficeRequestAuditService {
    private final BackOfficeRequestAuditRepository requestRepository;

    public void saveBackOfficeRequestAudit(Object request, String key, EventType eventType, String topic,
                                           RecordMetadata metadata, Throwable exception) {
        BackOfficeRequestAudit audit;
        if (BusinessUtil.isNotNull(metadata)){
            audit = constructSuccessAudit(metadata, key, eventType, request);
        } else {
            audit = constructFailureAudit(topic, key, eventType, request, exception);
        }
        requestRepository.save(audit);
    }

    private BackOfficeRequestAudit constructSuccessAudit(RecordMetadata recordMetadata, String identifier, EventType eventType,
                                                         Object request) {
        BackOfficeRequestAudit audit = constructAudit(recordMetadata.topic(), identifier, eventType, request, OperationStatus.SUCCESS);
        audit.setPartition(String.valueOf(recordMetadata.partition()));
        audit.setOffset(String.valueOf(recordMetadata.offset()));
        return audit;
    }

    private BackOfficeRequestAudit constructFailureAudit(String topic, String identifier, EventType eventType,
                                                         Object request, Throwable exception) {
        BackOfficeRequestAudit audit = constructAudit(topic, identifier, eventType, request, OperationStatus.FAILURE);
        audit.setExceptionMessage(exception.getMessage());
        return audit;
    }

    private BackOfficeRequestAudit constructAudit(String topicName, String identifier, EventType eventType,
                                                  Object request, OperationStatus status) {
        BackOfficeRequestAudit audit = new BackOfficeRequestAudit();
        audit.setTopicName(topicName);
        audit.setIdentifier(identifier);
        audit.setStatus(status.name());
        audit.setEventType(eventType.name());
        audit.setRequestSentTimestamp(BusinessUtil.getCurrentTimeStamp());
        audit.setDate(BusinessUtil.getCurrentDate());
        try {
            audit.setMessageData(JsonUtilityHelper.convertToString(request));
        } catch (ObjectMappingException e) {
            audit.setMessageData("");
        }
        return audit;
    }
}
