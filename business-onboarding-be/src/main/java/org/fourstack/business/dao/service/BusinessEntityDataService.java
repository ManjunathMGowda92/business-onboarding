package org.fourstack.business.dao.service;

import lombok.RequiredArgsConstructor;
import org.fourstack.business.dao.repository.BusinessEntityRepository;
import org.fourstack.business.entity.BusinessEntity;
import org.fourstack.business.enums.EntityStatus;
import org.fourstack.business.mapper.EntityMapper;
import org.fourstack.business.model.BusinessRegisterRequest;
import org.fourstack.business.model.Institute;
import org.fourstack.business.utils.KeyGenerationUtil;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor(onConstructor_ = @Lazy)
public class BusinessEntityDataService {
    private final EntityMapper entityMapper;
    private final BusinessEntityRepository businessRepository;

    public BusinessEntity createBusinessEntity(BusinessRegisterRequest request) {
        Institute institute = request.getInstitute();
        BusinessEntity entity = entityMapper.constructBusinessEntity(request);
        return saveEntity(entity, institute.getLei().getValue(), institute.getObjectId(), EntityStatus.INACTIVE);
    }

    private BusinessEntity saveEntity(BusinessEntity entity, String leiValue, String objectId, EntityStatus status) {
        String entityKey = KeyGenerationUtil.generateBusinessEntityKey(leiValue, objectId);
        entity.setKey(entityKey);
        entity.setStatus(status);
        businessRepository.save(entity);
        return entity;
    }

    public List<BusinessEntity> retrieveBusinessEntities(String leiValue) {
        return businessRepository.findAllByInstituteLeiValue(leiValue);
    }

    public Optional<BusinessEntity> retrieveBusiness(String leiValue, String orgId) {
        String entityKey = KeyGenerationUtil.generateBusinessEntityKey(leiValue, orgId);
        return businessRepository.findById(entityKey);
    }

    public Optional<BusinessEntity> retrieveBusiness(String entityKey) {
        return businessRepository.findById(entityKey);
    }
}
