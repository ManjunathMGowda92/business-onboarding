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
import org.fourstack.backoffice.model.UpdateAiRequest;
import org.fourstack.backoffice.util.BackOfficeUtil;
import org.springframework.stereotype.Component;

import java.util.Arrays;
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

    public void updateAiEntity(AgentInstitutionEntity entity, UpdateAiRequest request) {
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
        entity.setCreatedTimeStamp(BackOfficeUtil.getCurrentTimeStamp());
        return entity;
    }


}
