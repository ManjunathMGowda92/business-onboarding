package org.fourstack.business.dao.service;

import lombok.RequiredArgsConstructor;
import org.fourstack.business.dao.repository.AiOrgMapEntityRepository;
import org.fourstack.business.entity.AiOrgMapEntity;
import org.fourstack.business.entity.MainOrgIdEntity;
import org.fourstack.business.enums.EntityStatus;
import org.fourstack.business.mapper.EntityMapper;
import org.fourstack.business.utils.KeyGenerationUtil;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(onConstructor_ = @Lazy)
public class AiOrgMapEntityService {
    private final AiOrgMapEntityRepository aiOrgRepository;
    private final EntityMapper entityMapper;

    public void createAiOrgMapEntity(MainOrgIdEntity orgIdEntity, String aiId) {
        AiOrgMapEntity aiOrgMapEntity = entityMapper.constructAiOrgMapEntity(orgIdEntity, aiId, EntityStatus.INACTIVE);
        String entityKey = KeyGenerationUtil.generateAiOrgEntityKey(aiId, aiOrgMapEntity.getOrgId());
        aiOrgMapEntity.setKey(entityKey);
        aiOrgRepository.save(aiOrgMapEntity);
    }
}
