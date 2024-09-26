package org.fourstack.backoffice.mapper;

import org.fourstack.backoffice.entity.AiEntity;
import org.fourstack.backoffice.entity.AiOuMappingEntity;
import org.fourstack.backoffice.entity.OuEntity;
import org.fourstack.backoffice.model.AiOuMappingRequest;
import org.fourstack.backoffice.model.AiRequest;
import org.fourstack.backoffice.model.OuRequest;
import org.fourstack.backoffice.util.BackOfficeUtil;

public class EntityMapper {

    public AiEntity convertToAiEntity(AiRequest request) {
        AiEntity entity = new AiEntity();
        entity.setId(request.getAiId());
        entity.setName(request.getName());
        entity.setDescription(request.getDescription());
        entity.setSubscriberId(request.getSubscriberId());
        entity.setWebhookUrl(request.getAiWebhookUrl());
        entity.setHeadQuarter(request.getHeadquarter());
        entity.setCreatedTimeStamp(BackOfficeUtil.getCurrentTimeStamp());
        return entity;
    }

    public OuEntity convertToOuEntity(OuRequest request) {
        OuEntity entity = new OuEntity();
        entity.setId(request.getOuId());
        entity.setName(request.getName());
        entity.setDescription(request.getDescription());
        entity.setIfscCode(request.getIfscCode());
        entity.setBusinessVPA(request.getBusinessVPA());
        entity.setBranchLocation(request.getBranchLocation());
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
