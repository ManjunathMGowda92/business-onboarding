package org.fourstack.business.dao.service;

import lombok.RequiredArgsConstructor;
import org.fourstack.business.dao.repository.OrgEntityTransactionRepository;
import org.fourstack.business.dao.repository.OrgIdEntityRepository;
import org.fourstack.business.entity.BusinessEntity;
import org.fourstack.business.entity.MainOrgIdEntity;
import org.fourstack.business.entity.OrgIdTransactionEntity;
import org.fourstack.business.enums.EntityStatus;
import org.fourstack.business.mapper.EntityMapper;
import org.fourstack.business.model.Institute;
import org.fourstack.business.utils.KeyGenerationUtil;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor(onConstructor_ = @Lazy)
public class OrgEntityDataService {
    private final EntityMapper entityMapper;
    private final OrgIdEntityRepository orgEntityRepository;
    private final OrgEntityTransactionRepository orgTransactionRepository;

    public void createOrgIdEntity(BusinessEntity businessEntity, String aiId, String txnId) {
        Institute institute = businessEntity.getInstitute();
        String businessEntityKey = KeyGenerationUtil.generateBusinessEntityKey(institute.getLei().getValue(),
                institute.getObjectId());
        MainOrgIdEntity orgIdEntity = entityMapper.constructOrgIdEntity(businessEntity, businessEntityKey,
                aiId, EntityStatus.INACTIVE, txnId);
        String entityKey = KeyGenerationUtil.generateOrgIdEntityKey(orgIdEntity.getOrgId());
        orgIdEntity.setKey(entityKey);
        orgEntityRepository.save(orgIdEntity);

        createOrgTransactionData(businessEntity.getTxn().getId(), orgIdEntity, EntityStatus.INACTIVE);
    }

    public Optional<MainOrgIdEntity> retrieveOrgIdEntity(String orgId) {
        String entityKey = KeyGenerationUtil.generateOrgIdEntityKey(orgId);
        return orgEntityRepository.findById(entityKey);
    }

    public void createOrgTransactionData(String txnId, MainOrgIdEntity orgIdEntity, EntityStatus status) {
        String entityKey = KeyGenerationUtil.generateOrgIdTransactionKey(orgIdEntity.getOrgId(), txnId);
        OrgIdTransactionEntity entity = entityMapper.constructOrgTransactionEntity(orgIdEntity, status);
        entity.setKey(entityKey);
        orgTransactionRepository.save(entity);
    }
}
