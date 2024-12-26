package org.fourstack.business.dao.service;

import lombok.RequiredArgsConstructor;
import org.fourstack.business.dao.repository.AiOrgMapEntityRepository;
import org.fourstack.business.entity.business.AiOrgMapEntity;
import org.fourstack.business.entity.business.BusinessEntity;
import org.fourstack.business.enums.EntityStatus;
import org.fourstack.business.mapper.EntityMapper;
import org.fourstack.business.utils.KeyGenerationUtil;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor(onConstructor_ = @Lazy)
public class AiOrgMapEntityService {
    private final AiOrgMapEntityRepository aiOrgRepository;
    private final EntityMapper entityMapper;

    public void createAiOrgMapEntity(BusinessEntity businessEntity, String aiId) {
        AiOrgMapEntity aiOrgMapEntity = entityMapper.constructAiOrgMapEntity(businessEntity, aiId, EntityStatus.INACTIVE);
        String businessEntityKey = KeyGenerationUtil.generateBusinessEntityKey(businessEntity.getInstitute().getLei().getValue(),
                businessEntity.getInstitute().getObjectId());
        aiOrgMapEntity.setBusinessKey(businessEntityKey);
        String entityKey = KeyGenerationUtil.generateAiOrgEntityKey(aiId, aiOrgMapEntity.getOrgId());
        aiOrgMapEntity.setKey(entityKey);
        aiOrgRepository.save(aiOrgMapEntity);
    }

    public Optional<AiOrgMapEntity> retrieveAiOrgMapEntity(String aiId, String objectId) {
        String entityKey = KeyGenerationUtil.generateAiOrgEntityKey(aiId, objectId);
        return aiOrgRepository.findById(entityKey);
    }
}
