package org.fourstack.backoffice.mapper;

import org.fourstack.backoffice.entity.AgentInstitutionEntity;
import org.fourstack.backoffice.entity.AiOuMappingEntity;
import org.fourstack.backoffice.entity.OperationUnitEntity;
import org.fourstack.backoffice.enums.AiType;
import org.fourstack.backoffice.enums.EntityStatus;
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
import org.fourstack.backoffice.util.BackOfficeUtil;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
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
}
