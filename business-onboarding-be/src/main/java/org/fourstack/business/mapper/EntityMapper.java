package org.fourstack.business.mapper;

import org.fourstack.business.constants.BusinessConstants;
import org.fourstack.business.entity.AiEntity;
import org.fourstack.business.entity.AiOuMapEntity;
import org.fourstack.business.entity.AuditTransactionEntity;
import org.fourstack.business.entity.B2BIdentifierEntity;
import org.fourstack.business.entity.BusinessEntity;
import org.fourstack.business.entity.BusinessIdentifierEntity;
import org.fourstack.business.entity.OrgIdEntity;
import org.fourstack.business.entity.OrgIdTransactionEntity;
import org.fourstack.business.entity.OuEntity;
import org.fourstack.business.entity.TransactionEntity;
import org.fourstack.business.enums.B2BCreationReason;
import org.fourstack.business.enums.BusinessRole;
import org.fourstack.business.enums.EntityStatus;
import org.fourstack.business.enums.EventType;
import org.fourstack.business.enums.PrivacyType;
import org.fourstack.business.enums.TransactionFlow;
import org.fourstack.business.enums.TransactionStatus;
import org.fourstack.business.enums.TransactionSubStatus;
import org.fourstack.business.enums.TransactionType;
import org.fourstack.business.model.AiDetails;
import org.fourstack.business.model.AiOuMappingDetails;
import org.fourstack.business.model.B2BId;
import org.fourstack.business.model.BusinessIdentifier;
import org.fourstack.business.model.BusinessRegisterRequest;
import org.fourstack.business.model.CommonRequestData;
import org.fourstack.business.model.ContactNumber;
import org.fourstack.business.model.Institute;
import org.fourstack.business.model.MessageTransaction;
import org.fourstack.business.model.OuDetails;
import org.fourstack.business.model.RequesterB2B;
import org.fourstack.business.model.TransactionError;
import org.fourstack.business.utils.BusinessUtil;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class EntityMapper {

    public AiEntity generateAiEntity(AiDetails aiDetails) {
        AiEntity entity = new AiEntity();
        constructAiEntity(aiDetails, entity);
        entity.setCreatedTimeStamp(BusinessUtil.getCurrentTimeStamp());
        return entity;
    }

    public AiEntity updateAiEntity(AiDetails aiDetails, AiEntity dbEntity) {
        constructAiEntity(aiDetails, dbEntity);
        dbEntity.setLastModifiedTimeStamp(BusinessUtil.getCurrentTimeStamp());
        return dbEntity;
    }

    private void constructAiEntity(AiDetails aiDetails, AiEntity dbEntity) {
        dbEntity.setId(aiDetails.getAiId());
        dbEntity.setName(aiDetails.getName());
        dbEntity.setSubscriberId(aiDetails.getSubscriberId());
        dbEntity.setStatus(getEntityStatus(aiDetails.getStatus()));
    }

    private EntityStatus getEntityStatus(String status) {
        for (EntityStatus value : EntityStatus.values()) {
            if (value.name().equals(status)) {
                return value;
            }
        }
        return EntityStatus.INACTIVE;
    }

    public OuEntity updateOuEntity(OuDetails ouDetails, OuEntity dbEntity) {
        constructOuEntity(ouDetails, dbEntity);
        dbEntity.setLastModifiedTimeStamp(BusinessUtil.getCurrentTimeStamp());
        return dbEntity;
    }

    private void constructOuEntity(OuDetails ouDetails, OuEntity dbEntity) {
        dbEntity.setId(ouDetails.getOuId());
        dbEntity.setName(ouDetails.getName());
        dbEntity.setStatus(getEntityStatus(ouDetails.getStatus()));
    }

    public OuEntity generateOuEntity(OuDetails ouDetails) {
        OuEntity entity = new OuEntity();
        constructOuEntity(ouDetails, entity);
        entity.setCreatedTimeStamp(BusinessUtil.getCurrentTimeStamp());
        return entity;
    }

    public AiOuMapEntity generateAiOuEntity(AiOuMappingDetails aiOuDetails) {
        AiOuMapEntity entity = new AiOuMapEntity();
        constructAiOuEntity(aiOuDetails, entity);
        entity.setCreatedTimeStamp(BusinessUtil.getCurrentTimeStamp());
        return entity;
    }

    private void constructAiOuEntity(AiOuMappingDetails aiOuDetails, AiOuMapEntity entity) {
        entity.setAiId(aiOuDetails.getAiId());
        entity.setOuId(aiOuDetails.getOuId());
        entity.setStatus(getEntityStatus(aiOuDetails.getStatus()));
        entity.setWebhookUrl(aiOuDetails.getWebhookUrl());
    }

    public AiOuMapEntity updateAiOuEntity(AiOuMappingDetails aiOuDetails, AiOuMapEntity entity) {
        constructAiOuEntity(aiOuDetails, entity);
        entity.setLastModifiedTimeStamp(BusinessUtil.getCurrentTimeStamp());
        return entity;
    }

    public BusinessIdentifierEntity constructIdentifierEntity(String role, String aiId, String orgId,
                                                              BusinessIdentifier identifier) {
        BusinessIdentifierEntity entity = new BusinessIdentifierEntity();
        entity.setBusinessRole(role);
        entity.setAiId(aiId);
        entity.setOrgId(orgId);
        entity.setIdentifier(identifier);
        entity.setCreatedTimeStamp(BusinessUtil.getCurrentTimeStamp());
        return entity;
    }

    public BusinessEntity constructBusinessEntity(BusinessRegisterRequest request) {
        BusinessEntity entity = new BusinessEntity();
        CommonRequestData commonData = request.getCommonData();
        entity.setHead(commonData.getHead());
        entity.setTxn(commonData.getTxn());
        entity.setInstitute(request.getInstitute());
        entity.setDevice(commonData.getDevice());
        entity.setAdditionalInfoList(request.getAdditionalInfoList());
        entity.setCreatedTimeStamp(BusinessUtil.getCurrentTimeStamp());
        entity.setBusinessRole(getBusinessRole(request.getInstitute().getPrimaryIdentifier()));
        return entity;
    }

    public OrgIdEntity consrtuctOrgIdEntity(BusinessEntity entity, String businessKey) {
        OrgIdEntity orgIdEntity = new OrgIdEntity();
        orgIdEntity.setBusinessRole(entity.getBusinessRole());
        orgIdEntity.setBusinessKey(businessKey);

        Institute institute = entity.getInstitute();
        orgIdEntity.setOrgId(institute.getObjectId());
        orgIdEntity.setBusinessName(institute.getName());
        orgIdEntity.setLeiValue(institute.getLei().getValue());
        orgIdEntity.setLeiDocName(institute.getLei().getDocumentName());
        orgIdEntity.setBusinessType(institute.getLei().getType());
        orgIdEntity.setDefaultB2BId(institute.getDefaultB2bId());
        orgIdEntity.setPrimaryContactNumber(institute.getPrimaryContact().getPhoneNumber());
        orgIdEntity.setPrimaryEmail(institute.getPrimaryEmail());
        orgIdEntity.setAiId(entity.getHead().getAiId());
        orgIdEntity.setOuId(entity.getHead().getOuId());
        orgIdEntity.setProductType(entity.getHead().getProdType());

        addPublicB2bIds(orgIdEntity, institute);
        addIdentifiers(orgIdEntity, institute);
        addEmails(orgIdEntity, institute);
        addContactNumbers(orgIdEntity, institute);
        orgIdEntity.setCreatedTimeStamp(BusinessUtil.getCurrentTimeStamp());
        return orgIdEntity;
    }

    private void addContactNumbers(OrgIdEntity orgIdEntity, Institute institute) {
        if (BusinessUtil.isCollectionNullOrEmpty(orgIdEntity.getContactNumbers())) {
            orgIdEntity.setContactNumbers(new HashSet<>());
        }
        if (BusinessUtil.isCollectionNotNullOrEmpty(institute.getContactNumbers())) {
            Set<String> contactNumbers = institute.getContactNumbers().
                    stream().map(ContactNumber::getPhoneNumber)
                    .collect(Collectors.toSet());
            orgIdEntity.getContactNumbers().addAll(contactNumbers);
        }
    }

    private void addEmails(OrgIdEntity orgIdEntity, Institute institute) {
        if (BusinessUtil.isCollectionNullOrEmpty(orgIdEntity.getEmails())) {
            orgIdEntity.setEmails(new HashSet<>());
        }
        if (BusinessUtil.isCollectionNotNullOrEmpty(institute.getEmails())) {
            orgIdEntity.getEmails().addAll(institute.getEmails());
        }
    }

    private void addIdentifiers(OrgIdEntity orgIdEntity, Institute institute) {
        orgIdEntity.setPrimaryIdentifier(getIdentifier(institute.getPrimaryIdentifier()));
        if (BusinessUtil.isCollectionNullOrEmpty(orgIdEntity.getOtherIdentifiers())) {
            orgIdEntity.setOtherIdentifiers(new HashSet<>());
        }
        if (BusinessUtil.isCollectionNotNullOrEmpty(institute.getOtherIdentifiers())) {
            Set<String> identifiers = institute.getOtherIdentifiers()
                    .stream().map(this::getIdentifier)
                    .collect(Collectors.toSet());
            orgIdEntity.getOtherIdentifiers().addAll(identifiers);
        }
    }

    private void addPublicB2bIds(OrgIdEntity orgIdEntity, Institute institute) {
        if (BusinessUtil.isCollectionNullOrEmpty(orgIdEntity.getPublicB2BIds())) {
            orgIdEntity.setPublicB2BIds(new HashSet<>());
        }
        orgIdEntity.getPublicB2BIds().add(institute.getDefaultB2bId());
    }

    private String getIdentifier(BusinessIdentifier identifier) {
        return identifier.getDocumentName() + ":" + identifier.getValue();
    }

    private String getBusinessRole(BusinessIdentifier primaryIdentifier) {
        String documentName = primaryIdentifier.getDocumentName();
        if (BusinessConstants.GSTIN.equals(documentName) || BusinessConstants.UDYAM.equals(documentName)) {
            return BusinessRole.SUPPLIER_AND_BUYER.getRole();
        }
        return BusinessRole.BUYER.getRole();
    }

    public MessageTransaction constructTransaction(String txnId, String messageId, EventType eventType,
                                                   TransactionType txnType, String timeStamp, Object request) {
        MessageTransaction transaction = new MessageTransaction(txnId, messageId, txnType, eventType, timeStamp);
        transaction.setRequest(request);
        return transaction;
    }

    public TransactionEntity generateTransactionEntity(MessageTransaction transaction) {
        TransactionEntity entity = new TransactionEntity(transaction.getTransactionId(),
                transaction.getTransactionType());
        entity.setStatus(TransactionStatus.CREATED);
        entity.setTxnErrors(new HashMap<>());
        entity.setCreatedTimeStamp(BusinessUtil.getCurrentTimeStamp());
        return entity;
    }

    public TransactionEntity updateTransactionEntity(TransactionEntity transactionEntity, TransactionStatus status,
                                                     TransactionSubStatus subStatus, List<TransactionError> errors) {
        transactionEntity.setStatus(status);
        transactionEntity.setSubStatus(subStatus);
        if (BusinessUtil.isCollectionNotNullOrEmpty(errors)) {
            Map<String, TransactionError> txnErrors = BusinessUtil.isNotNull(transactionEntity.getTxnErrors())
                    ? transactionEntity.getTxnErrors() : new HashMap<>();
            errors.forEach(error -> txnErrors.put(error.getErrorCode(), error));
            transactionEntity.setTxnErrors(txnErrors);
        }
        transactionEntity.setLastModifiedTimeStamp(BusinessUtil.getCurrentTimeStamp());
        return transactionEntity;
    }

    public AuditTransactionEntity generateAuditTransactionEntity(MessageTransaction transaction, TransactionFlow flowType) {
        AuditTransactionEntity entity = new AuditTransactionEntity();
        entity.setTransaction(transaction);
        entity.setTransactionId(transaction.getTransactionId());
        entity.setFlowType(flowType);
        entity.setCreatedTimeStamp(BusinessUtil.getCurrentTimeStamp());
        return entity;
    }

    public B2BIdentifierEntity constructB2BIdEntity(BusinessRegisterRequest request, String businessRole) {
        B2BIdentifierEntity entity = new B2BIdentifierEntity();
        Institute institute = request.getInstitute();
        entity.setOrgId(institute.getObjectId());
        entity.setB2bIdValue(institute.getDefaultB2bId());
        CommonRequestData commonData = request.getCommonData();
        entity.setPrimaryAiId(commonData.getHead().getAiId());
        entity.setPrimaryOuId(commonData.getHead().getOuId());
        entity.setBusinessRole(businessRole);
        RequesterB2B requesterB2B = new RequesterB2B();
        requesterB2B.setRequesterB2BId(institute.getDefaultB2bId());
        entity.setOnboardingB2BId(requesterB2B);
        B2BId b2BId = getB2BId(institute);
        entity.setB2BId(b2BId);
        entity.setCreatedTimeStamp(BusinessUtil.getCurrentTimeStamp());
        return entity;
    }

    private B2BId getB2BId(Institute institute) {
        B2BId b2BId = new B2BId();
        b2BId.setValue(institute.getDefaultB2bId());
        b2BId.setReason(B2BCreationReason.OTHER.name());
        b2BId.setDescription("Default B2B Id");
        b2BId.setPrivacyType(PrivacyType.PUBLIC.name());
        b2BId.setBusinessIdentifier(institute.getPrimaryIdentifier());
        return b2BId;
    }

    public B2BIdentifierEntity constructB2BIdEntity(String businessRole, String aiId, String ouId, String orgId,
                                                    RequesterB2B requesterB2B, B2BId b2BId) {
        B2BIdentifierEntity entity = new B2BIdentifierEntity();
        entity.setB2bIdValue(b2BId.getValue());
        entity.setPrimaryAiId(aiId);
        entity.setPrimaryOuId(ouId);
        entity.setBusinessRole(businessRole);
        entity.setOrgId(orgId);
        entity.setOnboardingB2BId(requesterB2B);
        entity.setB2BId(b2BId);
        entity.setCreatedTimeStamp(BusinessUtil.getCurrentTimeStamp());
        return entity;
    }

    public OrgIdTransactionEntity constructOrgTransactionEntity(OrgIdEntity orgIdEntity, EntityStatus status) {
        OrgIdTransactionEntity entity = new OrgIdTransactionEntity();
        entity.setStatus(status);
        entity.setCreatedTimeStamp(BusinessUtil.getCurrentTimeStamp());
        entity.setOrgIdEntity(orgIdEntity);
        return entity;
    }
}
