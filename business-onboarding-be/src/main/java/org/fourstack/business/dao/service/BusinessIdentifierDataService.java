package org.fourstack.business.dao.service;

import lombok.RequiredArgsConstructor;
import org.fourstack.business.dao.repository.BusinessIdentifierEntityRepository;
import org.fourstack.business.entity.OrgIdentifierEntity;
import org.fourstack.business.enums.EntityStatus;
import org.fourstack.business.mapper.EntityMapper;
import org.fourstack.business.model.BusinessIdentifier;
import org.fourstack.business.utils.BusinessUtil;
import org.fourstack.business.utils.KeyGenerationUtil;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor(onConstructor_ = @Lazy)
public class BusinessIdentifierDataService {
    private final EntityMapper entityMapper;
    private final BusinessIdentifierEntityRepository identifierEntityRepository;

    public void createBusinessIdentifier(String businessRole, String aiId, String orgId,
                                         BusinessIdentifier identifier) {
        OrgIdentifierEntity identifierEntity = entityMapper.constructIdentifierEntity(businessRole,
                aiId, orgId, identifier);
        identifierEntity.setStatus(EntityStatus.INACTIVE);
        updateEntityKey(identifierEntity);
        identifierEntityRepository.save(identifierEntity);
    }

    public void createBusinessIdentifiers(String businessRole, String aiId, String orgId,
                                          List<BusinessIdentifier> businessIdentifiers) {
        if (BusinessUtil.isCollectionNotNullOrEmpty(businessIdentifiers)) {
            List<OrgIdentifierEntity> identifierEntities = businessIdentifiers.stream()
                    .map(identifier -> entityMapper.constructIdentifierEntity(businessRole, aiId, orgId, identifier))
                    .map(entity -> {
                        entity.setStatus(EntityStatus.INACTIVE);
                        updateEntityKey(entity);
                        return entity;
                    }).toList();
            identifierEntityRepository.saveAll(identifierEntities);
        }
    }

    private void updateEntityKey(OrgIdentifierEntity entity) {
        BusinessIdentifier identifier = entity.getIdentifier();
        entity.setKey(KeyGenerationUtil.generateBusinessIdentifierKey(identifier.getDocumentName(),
                identifier.getValue()));
    }

    public Optional<OrgIdentifierEntity> retrieveIdentifierEntity(String identifierType, String identifierValue) {
        String entityKey = KeyGenerationUtil.generateBusinessIdentifierKey(identifierType, identifierValue);
        return identifierEntityRepository.findById(entityKey);
    }
}
