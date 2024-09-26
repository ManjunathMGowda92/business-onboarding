package org.fourstack.business.mapper;

import org.fourstack.business.constants.BusinessConstants;
import org.fourstack.business.entity.AiEntity;
import org.fourstack.business.entity.AiOrgMapEntity;
import org.fourstack.business.entity.AiOuMapEntity;
import org.fourstack.business.entity.AuditTransactionEntity;
import org.fourstack.business.entity.B2BIdEntity;
import org.fourstack.business.entity.B2BIdentifierEntity;
import org.fourstack.business.entity.BusinessEntity;
import org.fourstack.business.entity.BusinessIdentifierEntity;
import org.fourstack.business.entity.MainOrgIdEntity;
import org.fourstack.business.entity.OrgIdTransactionEntity;
import org.fourstack.business.entity.OuEntity;
import org.fourstack.business.entity.TransactionEntity;
import org.fourstack.business.enums.B2BCreationReason;
import org.fourstack.business.enums.BankAccountType;
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
import org.fourstack.business.model.BankAccount;
import org.fourstack.business.model.BusinessIdentifier;
import org.fourstack.business.model.BusinessRegisterRequest;
import org.fourstack.business.model.CommonRequestData;
import org.fourstack.business.model.ContactNumber;
import org.fourstack.business.model.EntityVersion;
import org.fourstack.business.model.Institute;
import org.fourstack.business.model.MessageTransaction;
import org.fourstack.business.model.OuDetails;
import org.fourstack.business.model.RequesterB2B;
import org.fourstack.business.model.TransactionError;
import org.fourstack.business.utils.BusinessUtil;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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

    public MainOrgIdEntity consrtuctOrgIdEntity(BusinessEntity entity, String businessKey) {
        MainOrgIdEntity orgIdEntity = new MainOrgIdEntity();
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
        orgIdEntity.setProductType(entity.getHead().getProdType());

        addPublicB2bIds(orgIdEntity, institute);
        addIdentifiers(orgIdEntity, institute);
        addEmails(orgIdEntity, institute);
        addContactNumbers(orgIdEntity, institute);
        orgIdEntity.setCreatedTimeStamp(BusinessUtil.getCurrentTimeStamp());
        return orgIdEntity;
    }

    private void addContactNumbers(MainOrgIdEntity orgIdEntity, Institute institute) {
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

    private void addEmails(MainOrgIdEntity orgIdEntity, Institute institute) {
        if (BusinessUtil.isCollectionNullOrEmpty(orgIdEntity.getEmails())) {
            orgIdEntity.setEmails(new HashSet<>());
        }
        if (BusinessUtil.isCollectionNotNullOrEmpty(institute.getEmails())) {
            orgIdEntity.getEmails().addAll(institute.getEmails());
        }
    }

    private void addIdentifiers(MainOrgIdEntity orgIdEntity, Institute institute) {
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

    private void addPublicB2bIds(MainOrgIdEntity orgIdEntity, Institute institute) {
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
        entity.setB2BId(getB2BId(institute));
        entity.setBankAccount(retrieveBankAccount(request.getInstitute().getBankAccounts()));
        addAiIdToStatusMap(commonData.getHead().getAiId(), entity, EntityStatus.INACTIVE);
        entity.setCreatedTimeStamp(BusinessUtil.getCurrentTimeStamp());
        return entity;
    }

    private BankAccount retrieveBankAccount(List<BankAccount> bankAccounts) {
        if (bankAccounts.size() == 1) {
            return bankAccounts.getFirst();
        }
        Optional<BankAccount> optionalBankAccount = bankAccounts.stream()
                .filter(bankAccount -> BankAccountType.DEFAULT.name().equals(bankAccount.getType()))
                .findFirst();
        return optionalBankAccount.orElse(null);
    }

    private B2BIdEntity getB2BId(Institute institute) {
        B2BIdEntity b2BId = new B2BIdEntity();
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
        entity.setB2BId(getB2bEntity(b2BId));
        entity.setBankAccount(b2BId.getBankAccount());
        addAiIdToStatusMap(aiId, entity, EntityStatus.ACTIVE);
        entity.setCreatedTimeStamp(BusinessUtil.getCurrentTimeStamp());
        return entity;
    }

    private void addAiIdToStatusMap(String aiId, B2BIdentifierEntity entity, EntityStatus entityStatus) {
        Map<String, EntityStatus> aiStatusMap = entity.getAiStatusMap();
        if (BusinessUtil.isNull(aiStatusMap)) {
            aiStatusMap = new HashMap<>();
        }
        aiStatusMap.put(aiId, entityStatus);
    }

    private B2BIdEntity getB2bEntity(B2BId b2BId) {
        B2BIdEntity b2BIdEntity = new B2BIdEntity();
        b2BIdEntity.setValue(b2BId.getValue());
        b2BIdEntity.setReason(b2BId.getReason());
        b2BIdEntity.setDescription(b2BId.getDescription());
        b2BIdEntity.setPrivacyType(b2BId.getPrivacyType());
        b2BIdEntity.setBusinessIdentifier(b2BId.getBusinessIdentifier());
        return b2BIdEntity;
    }

    public OrgIdTransactionEntity constructOrgTransactionEntity(MainOrgIdEntity orgIdEntity, EntityStatus status) {
        OrgIdTransactionEntity entity = new OrgIdTransactionEntity();
        entity.setStatus(status);
        entity.setCreatedTimeStamp(BusinessUtil.getCurrentTimeStamp());
        entity.setOrgIdEntity(orgIdEntity);
        return entity;
    }

    public AiOrgMapEntity constructAiOrgMapEntity(MainOrgIdEntity orgIdEntity, String aiId, EntityStatus entityStatus) {
        AiOrgMapEntity aiOrgMapEntity = new AiOrgMapEntity();
        aiOrgMapEntity.setBusinessKey(orgIdEntity.getBusinessKey());
        aiOrgMapEntity.setOrgId(orgIdEntity.getOrgId());
        aiOrgMapEntity.setBusinessName(orgIdEntity.getBusinessName());
        aiOrgMapEntity.setLeiValue(orgIdEntity.getLeiValue());
        aiOrgMapEntity.setLeiDocName(orgIdEntity.getLeiDocName());
        aiOrgMapEntity.setBusinessType(orgIdEntity.getBusinessType());
        aiOrgMapEntity.setBusinessRole(orgIdEntity.getBusinessRole());
        aiOrgMapEntity.setCurrentVersion(orgIdEntity.getCurrentVersion());
        aiOrgMapEntity.setActiveVersion(orgIdEntity.getActiveVersion());
        aiOrgMapEntity.setAiId(orgIdEntity.getAiId());
        aiOrgMapEntity.setProductType(orgIdEntity.getProductType());
        aiOrgMapEntity.setPrimaryIdentifier(orgIdEntity.getPrimaryIdentifier());
        addPreviousVersions(aiOrgMapEntity, orgIdEntity);
        aiOrgMapEntity.setPublicB2BIds(orgIdEntity.getPublicB2BIds());
        aiOrgMapEntity.setPrivateB2BIds(orgIdEntity.getPrivateB2BIds());
        aiOrgMapEntity.setOtherIdentifiers(orgIdEntity.getOtherIdentifiers());
        aiOrgMapEntity.setPrimaryContactNumber(orgIdEntity.getPrimaryContactNumber());
        aiOrgMapEntity.setContactNumbers(orgIdEntity.getContactNumbers());
        aiOrgMapEntity.setPrimaryEmail(orgIdEntity.getPrimaryEmail());
        aiOrgMapEntity.setEmails(orgIdEntity.getEmails());
        aiOrgMapEntity.setStatus(entityStatus);
        addAiIdToStatusMap(aiId, orgIdEntity, entityStatus);
        return aiOrgMapEntity;
    }

    private void addPreviousVersions(AiOrgMapEntity aiOrgMapEntity, MainOrgIdEntity orgIdEntity) {
        List<EntityVersion> previousVersions = orgIdEntity.getPreviousVersions();
        List<EntityVersion> newVersions = new ArrayList<>();
        if (BusinessUtil.isCollectionNotNullOrEmpty(newVersions)) {
            for (EntityVersion previousVersion : previousVersions) {
                if (previousVersion.getVersion() == orgIdEntity.getCurrentVersion()) {
                    EntityVersion version = new EntityVersion();
                    version.setVersion(previousVersion.getVersion());
                    version.setTxnId(previousVersion.getTxnId());
                    version.setStatus(EntityStatus.INACTIVE);
                }
            }
        }
        aiOrgMapEntity.setPreviousVersions(newVersions);
    }

    private void addAiIdToStatusMap(String aiId, MainOrgIdEntity entity, EntityStatus entityStatus) {
        Map<String, EntityStatus> aiStatusMap = entity.getAiStatusMap();
        if (BusinessUtil.isNull(aiStatusMap)) {
            aiStatusMap = new HashMap<>();
        }
        aiStatusMap.put(aiId, entityStatus);
    }
}
