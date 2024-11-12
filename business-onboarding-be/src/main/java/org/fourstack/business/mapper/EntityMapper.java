package org.fourstack.business.mapper;

import org.fourstack.business.constants.BusinessConstants;
import org.fourstack.business.entity.AiEntity;
import org.fourstack.business.entity.AiOrgMapEntity;
import org.fourstack.business.entity.AiOuMapEntity;
import org.fourstack.business.entity.AuditTransactionEntity;
import org.fourstack.business.entity.B2BIdEntity;
import org.fourstack.business.entity.B2BIdentifierEntity;
import org.fourstack.business.entity.BusinessEntity;
import org.fourstack.business.entity.OrgIdentifierEntity;
import org.fourstack.business.entity.MainOrgIdEntity;
import org.fourstack.business.entity.OrgIdTransactionEntity;
import org.fourstack.business.entity.OrgVersions;
import org.fourstack.business.entity.OuEntity;
import org.fourstack.business.entity.SearchIdentifier;
import org.fourstack.business.entity.TransactionEntity;
import org.fourstack.business.enums.AiType;
import org.fourstack.business.enums.B2BCreationReason;
import org.fourstack.business.enums.BankAccountType;
import org.fourstack.business.enums.BusinessRole;
import org.fourstack.business.enums.BusinessType;
import org.fourstack.business.enums.EntityStatus;
import org.fourstack.business.enums.EventType;
import org.fourstack.business.enums.PrivacyType;
import org.fourstack.business.enums.TransactionFlow;
import org.fourstack.business.enums.TransactionStatus;
import org.fourstack.business.enums.TransactionSubStatus;
import org.fourstack.business.enums.TransactionType;
import org.fourstack.business.model.Lei;
import org.fourstack.business.model.backoffice.AiDetails;
import org.fourstack.business.model.backoffice.AiOuMappingDetails;
import org.fourstack.business.model.B2BId;
import org.fourstack.business.model.BankAccount;
import org.fourstack.business.model.BusinessIdentifier;
import org.fourstack.business.model.BusinessRegisterRequest;
import org.fourstack.business.model.CommonRequestData;
import org.fourstack.business.model.ContactNumber;
import org.fourstack.business.model.EntityVersion;
import org.fourstack.business.model.Institute;
import org.fourstack.business.model.MessageTransaction;
import org.fourstack.business.model.backoffice.OuDetails;
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
        dbEntity.setAiType(getAiType(aiDetails.getType()));
    }

    private AiType getAiType(String type) {
        for (AiType value : AiType.values()) {
            if (value.name().equals(type)) {
                return value;
            }
        }
        return AiType.PARTICIPATING;
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
        entity.setEncryptionDetails(aiOuDetails.getEncryptionDetails());
    }

    public AiOuMapEntity updateAiOuEntity(AiOuMappingDetails aiOuDetails, AiOuMapEntity entity) {
        constructAiOuEntity(aiOuDetails, entity);
        entity.setLastModifiedTimeStamp(BusinessUtil.getCurrentTimeStamp());
        return entity;
    }

    public OrgIdentifierEntity constructIdentifierEntity(String role, String aiId, String orgId,
                                                         BusinessIdentifier identifier) {
        OrgIdentifierEntity entity = new OrgIdentifierEntity();
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
        Institute institute = request.getInstitute();
        String verificationLevel = getVerificationLevel(institute);
        institute.setVerificationLevel(verificationLevel);
        entity.setInstitute(institute);
        entity.setDevice(commonData.getDevice());
        entity.setAdditionalInfoList(request.getAdditionalInfoList());
        entity.setCreatedTimeStamp(BusinessUtil.getCurrentTimeStamp());
        entity.setBusinessRole(getBusinessRole(institute.getPrimaryIdentifier()));
        return entity;
    }

    private String getVerificationLevel(Institute institute) {
        if (BusinessUtil.isNotNull(institute) && BusinessUtil.isNotNullOrEmpty(institute.getVerificationLevel())) {
            return institute.getVerificationLevel();
        }
        return String.valueOf(5);
    }

    public MainOrgIdEntity constructOrgIdEntity(BusinessEntity entity, String businessKey, String aiId,
                                                EntityStatus status, String txnId) {
        MainOrgIdEntity orgIdEntity = new MainOrgIdEntity();
        orgIdEntity.setBusinessKey(businessKey);
        orgIdEntity.setBusinessRole(entity.getBusinessRole());

        Institute institute = entity.getInstitute();
        orgIdEntity.setOrgId(institute.getObjectId());
        orgIdEntity.setBusinessName(institute.getName());
        orgIdEntity.setLeiValue(institute.getLei().getValue());
        orgIdEntity.setLeiDocName(institute.getLei().getDocumentName());
        orgIdEntity.setLeiType(institute.getLei().getType());
        orgIdEntity.setBusinessType(getBusinessType(institute.getBusinessType()));
        orgIdEntity.setDefaultB2BId(institute.getDefaultB2bId());
        orgIdEntity.setAiId(entity.getHead().getAiId());
        orgIdEntity.setProductType(entity.getHead().getProdType());
        orgIdEntity.setCurrentVersion(1);
        addPublicB2bIds(orgIdEntity, Set.of(institute.getDefaultB2bId()));
        EntityVersion version = getEntityVersion(status, txnId, 1);
        addAiIdToStatusMap(aiId, orgIdEntity, status, version);
        orgIdEntity.setStatus(status);
        orgIdEntity.setCreatedTimeStamp(BusinessUtil.getCurrentTimeStamp());
        return orgIdEntity;
    }

    private EntityVersion getEntityVersion(EntityStatus status, String txnId, int versionNumber) {
        EntityVersion version = new EntityVersion();
        version.setTxnId(txnId);
        version.setStatus(status);
        version.setVersion(versionNumber);
        return version;
    }

    private BusinessType getBusinessType(String businessType) {
        for (BusinessType value : BusinessType.values()) {
            if (value.name().equals(businessType)) {
                return value;
            }
        }
        return BusinessType.MICRO;
    }

    private void addContactNumbers(AiOrgMapEntity orgIdEntity, Institute institute) {
        if (BusinessUtil.isCollectionNullOrEmpty(orgIdEntity.getContactNumbers())) {
            orgIdEntity.setContactNumbers(new HashSet<>());
        }
        if (BusinessUtil.isCollectionNotNullOrEmpty(institute.getContactNumbers())) {
            orgIdEntity.getContactNumbers().addAll(institute.getContactNumbers());
        }
    }

    private void addEmails(AiOrgMapEntity orgIdEntity, Institute institute) {
        if (BusinessUtil.isCollectionNullOrEmpty(orgIdEntity.getEmails())) {
            orgIdEntity.setEmails(new HashSet<>());
        }
        if (BusinessUtil.isCollectionNotNullOrEmpty(institute.getEmails())) {
            orgIdEntity.getEmails().addAll(institute.getEmails());
        }
    }

    private void addIdentifiers(AiOrgMapEntity orgIdEntity, Institute institute) {
        orgIdEntity.setPrimaryIdentifier(institute.getPrimaryIdentifier());
        if (BusinessUtil.isCollectionNullOrEmpty(orgIdEntity.getOtherIdentifiers())) {
            orgIdEntity.setOtherIdentifiers(new HashSet<>());
        }
        if (BusinessUtil.isCollectionNotNullOrEmpty(institute.getOtherIdentifiers())) {
            orgIdEntity.getOtherIdentifiers().addAll(institute.getOtherIdentifiers());
        }
    }

    private void addPublicB2bIds(MainOrgIdEntity orgIdEntity, Set<String> b2bIds) {
        Set<String> publicB2BIds = BusinessUtil.isCollectionNotNullOrEmpty(orgIdEntity.getPublicB2BIds())
                ? orgIdEntity.getPublicB2BIds() : new HashSet<>();
        if (BusinessUtil.isCollectionNotNullOrEmpty(b2bIds)) {
            publicB2BIds.addAll(b2bIds);
        }
        orgIdEntity.setPublicB2BIds(publicB2BIds);
    }

    private void addPrivateB2bIds(MainOrgIdEntity orgIdEntity, Set<String> b2bIds) {
        Set<String> privateB2BIds = BusinessUtil.isCollectionNotNullOrEmpty(orgIdEntity.getPrivateB2BIds())
                ? orgIdEntity.getPrivateB2BIds() : new HashSet<>();
        if (BusinessUtil.isCollectionNotNullOrEmpty(b2bIds)) {
            privateB2BIds.addAll(b2bIds);
        }
        orgIdEntity.setPrivateB2BIds(privateB2BIds);
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

    public AiOrgMapEntity constructAiOrgMapEntity(BusinessEntity businessEntity, String aiId, EntityStatus entityStatus) {
        AiOrgMapEntity aiOrgMapEntity = new AiOrgMapEntity();
        Institute institute = businessEntity.getInstitute();
        aiOrgMapEntity.setOrgId(institute.getObjectId());
        aiOrgMapEntity.setBusinessName(institute.getName());
        populateLeiDetails(aiOrgMapEntity, institute.getLei());
        aiOrgMapEntity.setBusinessType(getBusinessType(institute.getBusinessType()));
        aiOrgMapEntity.setBusinessRole(businessEntity.getBusinessRole());
        aiOrgMapEntity.setCurrentVersion(1);
        aiOrgMapEntity.setActiveVersion(1);
        aiOrgMapEntity.setAiId(aiId);
        aiOrgMapEntity.setProductType(businessEntity.getHead().getProdType());
        populatePublicB2BIds(aiOrgMapEntity, Set.of(institute.getDefaultB2bId()));
        aiOrgMapEntity.setVerificationLevel(BusinessUtil.convertToInt(institute.getVerificationLevel(), 5));
        populateBusinessIdentifiers(aiOrgMapEntity, institute);
        populateContactNumbers(aiOrgMapEntity, institute);
        populateBankAccounts(institute, aiOrgMapEntity);
        populateEmails(aiOrgMapEntity, institute);
        aiOrgMapEntity.setStatus(entityStatus);
        aiOrgMapEntity.setCreatedTimeStamp(BusinessUtil.getCurrentTimeStamp());
        return aiOrgMapEntity;
    }

    private void populateEmails(AiOrgMapEntity aiOrgMapEntity, Institute institute) {
        aiOrgMapEntity.setPrimaryEmail(institute.getPrimaryEmail());
        if (BusinessUtil.isCollectionNotNullOrEmpty(institute.getEmails())) {
            HashSet<String> emails = new HashSet<>(institute.getEmails());
            aiOrgMapEntity.setEmails(emails);
        } else {
            aiOrgMapEntity.setEmails(new HashSet<>());
        }
    }

    private void populateBankAccounts(Institute institute, AiOrgMapEntity aiOrgMapEntity) {
        if (BusinessUtil.isCollectionNotNullOrEmpty(institute.getBankAccounts())) {
            HashSet<BankAccount> bankAccounts = new HashSet<>(institute.getBankAccounts());
            aiOrgMapEntity.setBankAccounts(bankAccounts);
        } else {
            aiOrgMapEntity.setBankAccounts(new HashSet<>());
        }
    }

    private void populateContactNumbers(AiOrgMapEntity aiOrgMapEntity, Institute institute) {
        aiOrgMapEntity.setPrimaryContactNumber(institute.getPrimaryContact());
        if (BusinessUtil.isCollectionNotNullOrEmpty(institute.getContactNumbers())) {
            HashSet<ContactNumber> contactNumbers = new HashSet<>(institute.getContactNumbers());
            aiOrgMapEntity.setContactNumbers(contactNumbers);
        } else {
            aiOrgMapEntity.setContactNumbers(new HashSet<>());
        }
    }

    private void populateBusinessIdentifiers(AiOrgMapEntity aiOrgMapEntity, Institute institute) {
        aiOrgMapEntity.setPrimaryIdentifier(institute.getPrimaryIdentifier());
        if (BusinessUtil.isCollectionNotNullOrEmpty(institute.getOtherIdentifiers())) {
            HashSet<BusinessIdentifier> identifiers = new HashSet<>(institute.getOtherIdentifiers());
            aiOrgMapEntity.setOtherIdentifiers(identifiers);
        } else {
            aiOrgMapEntity.setOtherIdentifiers(new HashSet<>());
        }
    }

    private void populatePublicB2BIds(AiOrgMapEntity aiOrgMapEntity, Set<String> b2bIds) {
        Set<String> publicB2BIds = BusinessUtil.isCollectionNotNullOrEmpty(aiOrgMapEntity.getPublicB2BIds())
                ? aiOrgMapEntity.getPublicB2BIds() : new HashSet<>();
        publicB2BIds.addAll(b2bIds);
        aiOrgMapEntity.setPublicB2BIds(publicB2BIds);
    }

    private void populatePrivateB2BIds(AiOrgMapEntity aiOrgMapEntity, Set<String> b2bIds) {
        Set<String> privateB2BIds = BusinessUtil.isCollectionNotNullOrEmpty(aiOrgMapEntity.getPrivateB2BIds())
                ? aiOrgMapEntity.getPrivateB2BIds() : new HashSet<>();
        privateB2BIds.addAll(b2bIds);
        aiOrgMapEntity.setPrivateB2BIds(privateB2BIds);
    }

    private void populateLeiDetails(AiOrgMapEntity aiOrgMapEntity, Lei lei) {
        if (BusinessUtil.isNotNull(lei)) {
            aiOrgMapEntity.setLeiDocName(lei.getDocumentName());
            aiOrgMapEntity.setLeiValue(lei.getValue());
            aiOrgMapEntity.setLeiType(lei.getType());
        }
    }

    private void addAiIdToStatusMap(String aiId, MainOrgIdEntity entity, EntityStatus entityStatus, EntityVersion version) {
        Map<String, OrgVersions> aiStatusMap = BusinessUtil.isNotNull(entity.getAiStatusMap())
                ? entity.getAiStatusMap() : new HashMap<>();
        OrgVersions aiOrgVersion = aiStatusMap.getOrDefault(aiId, new OrgVersions());
        aiOrgVersion.setAiOrgStatus(entityStatus);
        List<EntityVersion> previousVersions = BusinessUtil.isCollectionNotNullOrEmpty(aiOrgVersion.getPreviousVersions())
                ? aiOrgVersion.getPreviousVersions() : new ArrayList<>();
        previousVersions.add(version);
        aiOrgVersion.setPreviousVersions(previousVersions);
    }

    public SearchIdentifier constructSearchIdentifier(String identifierType, String identifierValue, String objectId) {
        SearchIdentifier identifier = new SearchIdentifier();
        identifier.setIdentifierType(identifierType);
        identifier.setIdentifierValue(identifierValue);
        Set<String> businessIds = BusinessUtil.isCollectionNotNullOrEmpty(identifier.getBusinessIds())
                ? identifier.getBusinessIds() : new HashSet<>();
        businessIds.add(objectId);
        identifier.setBusinessIds(businessIds);
        identifier.setCreatedTimeStamp(BusinessUtil.getCurrentTimeStamp());
        return identifier;
    }
}
