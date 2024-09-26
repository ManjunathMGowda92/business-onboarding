package org.fourstack.business.dao.service;

import lombok.RequiredArgsConstructor;
import org.fourstack.business.dao.repository.AuditTransactionEntityRepository;
import org.fourstack.business.dao.repository.TransactionEntityRepository;
import org.fourstack.business.entity.AuditTransactionEntity;
import org.fourstack.business.entity.TransactionEntity;
import org.fourstack.business.enums.TransactionFlow;
import org.fourstack.business.enums.TransactionStatus;
import org.fourstack.business.enums.TransactionSubStatus;
import org.fourstack.business.mapper.EntityMapper;
import org.fourstack.business.model.MessageTransaction;
import org.fourstack.business.model.TransactionError;
import org.fourstack.business.utils.BusinessUtil;
import org.fourstack.business.utils.KeyGenerationUtil;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor(onConstructor_ = @Lazy)
public class TransactionDataService {
    private final TransactionEntityRepository transactionRepository;
    private final AuditTransactionEntityRepository auditTransactionRepository;
    private final EntityMapper entityMapper;

    private static final Set<TransactionStatus> exceptionStatusSet = Set.of(TransactionStatus.FAILED, TransactionStatus.OUTBOUND_FAILURE,
            TransactionStatus.INVALID);

    public void createTransactionEntity(MessageTransaction transaction, TransactionStatus status, TransactionSubStatus subStatus) {
        saveTransactionEntity(transaction, status, subStatus, Collections.emptyList());
    }

    public void updateTransactionEntity(MessageTransaction transaction, TransactionStatus status,
                                        TransactionSubStatus subStatus, List<TransactionError> txnErrors) {
        saveTransactionEntity(transaction, status, subStatus, txnErrors);
    }

    public void updateTransactionEntity(TransactionEntity transaction, TransactionStatus status,
                                        TransactionSubStatus subStatus, List<TransactionError> txnErrors) {
        entityMapper.updateTransactionEntity(transaction, status, subStatus, txnErrors);
        transactionRepository.save(transaction);
    }

    private void saveTransactionEntity(MessageTransaction transaction, TransactionStatus status,
                                       TransactionSubStatus subStatus, List<TransactionError> txnError) {
        String entityKey = KeyGenerationUtil.generateTransactionEntityKey(transaction.getTransactionId());
        Optional<TransactionEntity> optionalEntity = retrieveTransactionEntityByEntityKey(entityKey);
        TransactionEntity entity;
        if (optionalEntity.isEmpty()) {
            entity = entityMapper.generateTransactionEntity(transaction);
            entity.setStatus(status);
            entity.setSubStatus(subStatus);
        } else {
            TransactionStatus currentStatus = optionalEntity.get().getStatus();
            if (BusinessUtil.isNotNull(currentStatus) && exceptionStatusSet.contains(currentStatus)) {
                entity = entityMapper.updateTransactionEntity(optionalEntity.get(),
                        currentStatus, subStatus, txnError);
            } else {
                entity = entityMapper.updateTransactionEntity(optionalEntity.get(),
                        status, subStatus, txnError);
            }
        }
        entity.setKey(entityKey);
        transactionRepository.save(entity);
    }

    private Optional<TransactionEntity> retrieveTransactionEntityByEntityKey(String entityKey) {
        return transactionRepository.findById(entityKey);
    }

    public Optional<TransactionEntity> retrieveTransaction(String transactionId) {
        String entityKey = KeyGenerationUtil.generateTransactionEntityKey(transactionId);
        return retrieveTransactionEntityByEntityKey(entityKey);
    }

    public void saveAuditTransactionEntity(MessageTransaction transaction, TransactionFlow flowType) {
        AuditTransactionEntity entity = entityMapper.generateAuditTransactionEntity(transaction, flowType);
        String entityKey = KeyGenerationUtil.generateTxnAuditEntityKey(transaction.getTransactionId(), flowType.name());
        entity.setKey(entityKey);
        auditTransactionRepository.save(entity);
    }

    public void auditInboundTransaction(MessageTransaction transaction) {
        saveAuditTransactionEntity(transaction, TransactionFlow.INBOUND);
    }

    public void auditOutboundTransaction(MessageTransaction transaction) {
        saveAuditTransactionEntity(transaction, TransactionFlow.OUTBOUND);
    }
}
