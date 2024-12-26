package org.fourstack.backoffice.mapper;

import org.fourstack.backoffice.entity.AgentInstitutionEntity;
import org.fourstack.backoffice.entity.AiOuMappingEntity;
import org.fourstack.backoffice.entity.OperationUnitEntity;
import org.fourstack.backoffice.entity.kyc.KycRequestAuditEntity;
import org.fourstack.backoffice.entity.kyc.KycRequestEntity;
import org.fourstack.backoffice.enums.AiType;
import org.fourstack.backoffice.enums.EntityStatus;
import org.fourstack.backoffice.enums.KycRequestType;
import org.fourstack.backoffice.model.AiOuMappingRequest;
import org.fourstack.backoffice.model.AiRequest;
import org.fourstack.backoffice.model.EncryptionDetails;
import org.fourstack.backoffice.model.OuRequest;
import org.fourstack.backoffice.model.AiUpdateRequest;
import org.fourstack.backoffice.model.business.AiDetails;
import org.fourstack.backoffice.model.business.AiOuEncryptionDetails;
import org.fourstack.backoffice.model.business.AiOuMappingDetails;
import org.fourstack.backoffice.model.business.MasterDataRequest;
import org.fourstack.backoffice.model.business.OuDetails;
import org.fourstack.backoffice.model.business.kyc.BusinessIdentifier;
import org.fourstack.backoffice.model.business.kyc.EditBusinessIdentifier;
import org.fourstack.backoffice.model.business.kyc.EditInstitute;
import org.fourstack.backoffice.model.business.kyc.Institute;
import org.fourstack.backoffice.model.business.kyc.KycBusinessRequest;
import org.fourstack.backoffice.util.BackOfficeUtil;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
public class EntityMapper {

    public AgentInstitutionEntity convertToAiEntity(AiRequest request) {
        AgentInstitutionEntity entity = new AgentInstitutionEntity();
        entity.setId(request.getAgentInstitutionId());
        entity.setName(request.getAgentInstitutionName());
        entity.setAlias(request.getAgentInstitutionAliasName());
        entity.setDescription(request.getDescription());
        entity.setSubscriberId(request.getSubscriberId());
        entity.setType(getAiType(request.getType()));
        entity.setRegisteredAddress(request.getRegisteredAddress());
        entity.setCommunicationAddress(request.getCommunicationAddress());
        entity.setStatus(EntityStatus.ACTIVE);
        entity.setCreatedTimeStamp(BackOfficeUtil.getCurrentTimeStamp());
        return entity;
    }

    private AiType getAiType(String type) {
        Optional<AiType> optionalAiType = Arrays.stream(AiType.values())
                .filter(aiType -> aiType.getType().equals(type))
                .findFirst();
        return optionalAiType.orElse(AiType.NON_PARTICIPATING);
    }

    public void updateAiOuEntity(AiOuMappingEntity entity, EncryptionDetails encryptionDetails) {
        entity.setEncryptionDetails(encryptionDetails);
        entity.setLastModifiedTimeStamp(BackOfficeUtil.getCurrentTimeStamp());
    }

    public void updateAiEntity(AgentInstitutionEntity entity, AiUpdateRequest request) {
        if (BackOfficeUtil.isNotNull(request)) {
            entity.setLastModifiedTimeStamp(BackOfficeUtil.getCurrentTimeStamp());
            entity.setName(request.getAgentInstitutionName());
            if (BackOfficeUtil.isNotNullOrEmpty(request.getAgentInstitutionAliasName())) {
                entity.setAlias(request.getAgentInstitutionAliasName());
            }
            if (BackOfficeUtil.isNotNullOrEmpty(request.getDescription())) {
                entity.setDescription(request.getDescription());
            }
            entity.setType(getAiType(request.getType()));
            entity.setSubscriberId(request.getSubscriberId());
            entity.setRegisteredAddress(request.getRegisteredAddress());
            if (BackOfficeUtil.isNotNull(request.getCommunicationAddress())) {
                entity.setCommunicationAddress(request.getCommunicationAddress());
            }
        }
    }

    public OperationUnitEntity convertToOuEntity(OuRequest request) {
        OperationUnitEntity entity = new OperationUnitEntity();
        entity.setId(request.getOuId());
        entity.setName(request.getOperationUnitName());
        entity.setAlias(request.getOperationUnitAliasName());
        entity.setDescription(request.getDescription());
        entity.setMailId(request.getMailId());
        entity.setRegisteredAddress(request.getRegisteredAddress());
        entity.setCommunicationAddress(request.getCommunicationAddress());
        entity.setBankDetails(request.getBankDetails());
        entity.setStatus(EntityStatus.ACTIVE);
        entity.setCreatedTimeStamp(BackOfficeUtil.getCurrentTimeStamp());
        return entity;
    }

    public AiOuMappingEntity convertToAiOuMapEntity(AiOuMappingRequest request) {
        AiOuMappingEntity entity = new AiOuMappingEntity();
        entity.setAiId(request.getAiId());
        entity.setAiName(request.getAiName());
        entity.setOuId(request.getOuId());
        entity.setOuName(request.getOuName());
        entity.setDescription(request.getDescription());
        entity.setWebhookUrl(request.getWebhookUrl());
        entity.setEncryptionDetails(request.getEncryptionDetails());
        entity.setStatus(EntityStatus.ACTIVE);
        entity.setCreatedTimeStamp(BackOfficeUtil.getCurrentTimeStamp());
        return entity;
    }

    public AiDetails convertToAiDetails(AgentInstitutionEntity entity) {
        AiDetails details = new AiDetails();
        details.setAiId(entity.getId());
        details.setName(entity.getName());
        details.setSubscriberId(entity.getSubscriberId());
        details.setStatus(entity.getStatus().name());
        details.setType(entity.getType().getType());
        return details;
    }

    public OuDetails convertToOuDetails(OperationUnitEntity entity) {
        OuDetails details = new OuDetails();
        details.setOuId(entity.getId());
        details.setName(entity.getName());
        details.setStatus(entity.getStatus().name());
        return details;
    }

    public AiOuMappingDetails convertToAiOuDetails(AiOuMappingEntity entity) {
        AiOuMappingDetails details = new AiOuMappingDetails();
        details.setAiId(entity.getAiId());
        details.setOuId(entity.getOuId());
        details.setStatus(entity.getStatus().name());
        details.setWebhookUrl(entity.getWebhookUrl());
        details.setEncryptionDetails(constructEncryptionDetails(entity.getEncryptionDetails()));
        return details;
    }

    private AiOuEncryptionDetails constructEncryptionDetails(EncryptionDetails encryptionDetails) {
        if (BackOfficeUtil.isNotNull(encryptionDetails)) {
            AiOuEncryptionDetails details = new AiOuEncryptionDetails();
            details.setKey(encryptionDetails.getKey());
            details.setEffectiveStartDate(encryptionDetails.getEffectiveFrom());
            details.setEffectiveEndDate(encryptionDetails.getEffectiveTill());
            return details;
        }
        return null;
    }

    public MasterDataRequest constructMasterDataRequest(List<AiDetails> aiDetails, List<OuDetails> ouDetails,
                                                        List<AiOuMappingDetails> aiOuDetails) {
        return MasterDataRequest.builder()
                .aiDetails(aiDetails)
                .ouDetails(ouDetails)
                .aiOuDetails(aiOuDetails)
                .build();
    }

    public KycRequestEntity constructKycEntityForEditBusiness(KycBusinessRequest request, KycRequestEntity entity) {
        if (BackOfficeUtil.isNotNull(entity)) {
            constructCommonData(request, entity, KycRequestType.EDIT_BUSINESS.getRequestType());
            EditInstitute editInstitute = request.getEditInstitute();
            Institute institute = convertToInstitute(editInstitute);
            entity.setInstitute(institute);
            entity.setLastModifiedTimeStamp(BackOfficeUtil.getCurrentTimeStamp());
            return entity;
        } else {
            return constructKycEntityForNewBusiness(request);
        }
    }

    private void constructCommonData(KycBusinessRequest request, KycRequestEntity entity, String requestType) {
        entity.setRequestType(requestType);
        entity.setHead(request.getHead());
        entity.getHead().setOuId(null);
        entity.setTxn(request.getTxn());
        entity.setKycRequestedOuIds(request.getKycRequestedOuIds());
        entity.setAdditionalInfos(request.getAdditionalInfos());
    }

    private Institute convertToInstitute(EditInstitute editInstitute) {
        Institute institute = new Institute();
        institute.setObjectId(editInstitute.getObjectId());
        institute.setName(editInstitute.getName());
        institute.setAlias(editInstitute.getAlias());
        institute.setDefaultB2bId(editInstitute.getDefaultB2bId());
        institute.setMccCode(editInstitute.getMccCode());
        institute.setBusinessType(editInstitute.getBusinessType());
        institute.setVerificationLevel(editInstitute.getVerificationLevel());
        institute.setLei(editInstitute.getLei());
        institute.setAddresses(editInstitute.getAddresses());
        institute.setBankAccounts(editInstitute.getBankAccounts());
        institute.setContactNumbers(editInstitute.getContactNumbers());
        institute.setPrimaryContact(editInstitute.getPrimaryContact());
        institute.setEmails(editInstitute.getEmails());
        institute.setPrimaryEmail(editInstitute.getPrimaryEmail());
        updateBusinessIdentifiers(editInstitute, institute);
        return institute;
    }

    private void updateBusinessIdentifiers(EditInstitute editInstitute, Institute institute) {
        EditBusinessIdentifier primaryIdentifier = editInstitute.getPrimaryIdentifier();
        if (BackOfficeUtil.isNotNull(primaryIdentifier) && BackOfficeUtil.isNotNull(primaryIdentifier.getNewIdentifier())) {
            institute.setPrimaryIdentifier(primaryIdentifier.getNewIdentifier());
        }
        List<EditBusinessIdentifier> otherIdentifiers = editInstitute.getOtherIdentifiers();
        if (BackOfficeUtil.isCollectionNotNullOrEmpty(otherIdentifiers)) {
            List<BusinessIdentifier> identifiers = otherIdentifiers.stream()
                    .map(EditBusinessIdentifier::getNewIdentifier)
                    .filter(Objects::nonNull)
                    .toList();
            institute.setOtherIdentifiers(identifiers);
        }
    }

    public KycRequestEntity constructKycEntityForNewBusiness(KycBusinessRequest request) {
        KycRequestEntity entity = new KycRequestEntity();
        constructCommonData(request, entity, KycRequestType.CREATE_BUSINESS.getRequestType());
        entity.setObjectId(request.getInstitute().getObjectId());
        entity.setInstitute(request.getInstitute());
        entity.setCreatedTimeStamp(BackOfficeUtil.getCurrentTimeStamp());
        return entity;
    }

    public KycRequestAuditEntity constructKycAuditEntity(KycRequestEntity entity) {
        KycRequestAuditEntity auditEntity = new KycRequestAuditEntity();
        auditEntity.setObjectId(entity.getObjectId());
        auditEntity.setRequestType(entity.getRequestType());
        auditEntity.setHead(entity.getHead());
        auditEntity.setTxn(entity.getTxn());
        auditEntity.setInstitute(entity.getInstitute());
        auditEntity.setKycOuMap(entity.getKycOuMap());
        auditEntity.setAdditionalInfos(entity.getAdditionalInfos());
        auditEntity.setTimeStamp(BackOfficeUtil.getCurrentTimeStamp());
        return auditEntity;
    }
}
