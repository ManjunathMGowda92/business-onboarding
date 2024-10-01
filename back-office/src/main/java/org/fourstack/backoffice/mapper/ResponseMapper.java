package org.fourstack.backoffice.mapper;

import org.fourstack.backoffice.entity.AgentInstitutionEntity;
import org.fourstack.backoffice.model.AiResponse;

public class ResponseMapper {

    public AiResponse mapAiEntityToResponse(AgentInstitutionEntity aiEntity) {
        AiResponse response = new AiResponse();
        response.setAgentInstitutionId(aiEntity.getId());
        response.setAgentInstitutionName(aiEntity.getName());
        response.setAgentInstitutionAliasName(aiEntity.getAlias());
        response.setDescription(aiEntity.getDescription());
        response.setSubscriberId(aiEntity.getSubscriberId());
        response.setRegisteredAddress(aiEntity.getRegisteredAddress());
        response.setCommunicationAddress(aiEntity.getCommunicationAddress());
        response.setStatus(aiEntity.getStatus());
        response.setCreatedTimeStamp(aiEntity.getCreatedTimeStamp());
        response.setLastModifiedTimeStamp(aiEntity.getLastModifiedTimeStamp());
        response.setEncryptionDetails(aiEntity.getEncryptionDetails());
        response.setLinkedOus(aiEntity.getLinkedOus());
        return response;
    }
}
