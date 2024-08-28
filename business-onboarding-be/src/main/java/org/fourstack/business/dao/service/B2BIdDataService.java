package org.fourstack.business.dao.service;

import lombok.RequiredArgsConstructor;
import org.fourstack.business.dao.repository.B2BIdentifierEntityRepository;
import org.fourstack.business.entity.B2BIdentifierEntity;
import org.fourstack.business.enums.EntityStatus;
import org.fourstack.business.mapper.EntityMapper;
import org.fourstack.business.model.B2BId;
import org.fourstack.business.model.BusinessRegisterRequest;
import org.fourstack.business.model.RequesterB2B;
import org.fourstack.business.utils.BusinessUtil;
import org.fourstack.business.utils.KeyGenerationUtil;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor(onConstructor_ = @Lazy)
public class B2BIdDataService {
    private final EntityMapper entityMapper;
    private final B2BIdentifierEntityRepository b2bIdRepository;

    public void createB2BIdEntity(BusinessRegisterRequest request, String businessRole) {
        B2BIdentifierEntity entity = entityMapper.constructB2BIdEntity(request, businessRole);
        entity.setStatus(EntityStatus.INACTIVE);
        entity.setKey(KeyGenerationUtil.generateB2BIdentifierKey(entity.getB2bIdValue()));
        b2bIdRepository.save(entity);
    }

    public void createB2BIdEntities(String role, String aiId, String ouId, String orgId,
                                    RequesterB2B requesterB2B, List<B2BId> b2BIds) {
        if (BusinessUtil.isCollectionNotNullOrEmpty(b2BIds)) {
            List<B2BIdentifierEntity> identifierEntities = b2BIds.stream()
                    .map(b2BId -> entityMapper.constructB2BIdEntity(role, aiId, ouId, orgId, requesterB2B, b2BId))
                    .map(entity -> {
                        entity.setStatus(EntityStatus.ACTIVE);
                        entity.setKey(KeyGenerationUtil.generateB2BIdentifierKey(entity.getB2bIdValue()));
                        return entity;
                    })
                    .toList();
            b2bIdRepository.saveAll(identifierEntities);
        }
    }

    public Optional<B2BIdentifierEntity> retrieveB2BId(String b2bId) {
        String b2BIdentifierKey = KeyGenerationUtil.generateB2BIdentifierKey(b2bId);
        return b2bIdRepository.findById(b2BIdentifierKey);
    }
}
