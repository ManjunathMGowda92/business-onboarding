package org.fourstack.business.dao.service;

import lombok.RequiredArgsConstructor;
import org.fourstack.business.dao.repository.AiOrgMapEntityRepository;
import org.fourstack.business.entity.MainOrgIdEntity;
import org.fourstack.business.enums.EntityStatus;
import org.fourstack.business.mapper.EntityMapper;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(onConstructor_ = @Lazy)
public class AiOrgMapEntityService {
    private final AiOrgMapEntityRepository aiOrgRepository;
    private final EntityMapper entityMapper;

    public void createAiOrgMapEntity(MainOrgIdEntity orgIdEntity, String aiId) {
        entityMapper.constructAiOrgMapEntity(orgIdEntity, aiId, EntityStatus.INACTIVE);
    }
}
