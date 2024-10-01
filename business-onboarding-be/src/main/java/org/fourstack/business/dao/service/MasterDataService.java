package org.fourstack.business.dao.service;

import lombok.RequiredArgsConstructor;
import org.fourstack.business.dao.repository.AiAuditEntityRepository;
import org.fourstack.business.dao.repository.AiEntityRepository;
import org.fourstack.business.dao.repository.AiOuMapAuditEntityRepository;
import org.fourstack.business.dao.repository.AiOuMappingRepository;
import org.fourstack.business.dao.repository.OuAuditEntityRepository;
import org.fourstack.business.dao.repository.OuEntityRepository;
import org.fourstack.business.entity.AiAuditEntity;
import org.fourstack.business.entity.AiEntity;
import org.fourstack.business.entity.AiOuMapAuditEntity;
import org.fourstack.business.entity.AiOuMapEntity;
import org.fourstack.business.entity.OuAuditEntity;
import org.fourstack.business.entity.OuEntity;
import org.fourstack.business.mapper.EntityMapper;
import org.fourstack.business.model.AiDetails;
import org.fourstack.business.model.AiOuMappingDetails;
import org.fourstack.business.model.OuDetails;
import org.fourstack.business.utils.KeyGenerationUtil;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor(onConstructor_ = @Lazy)
public class MasterDataService {
    private final AiEntityRepository aiEntityRepository;
    private final OuEntityRepository ouEntityRepository;
    private final AiOuMappingRepository aiOuMappingRepository;
    private final AiAuditEntityRepository aiAuditEntityRepository;
    private final OuAuditEntityRepository ouAuditEntityRepository;
    private final AiOuMapAuditEntityRepository aiOuMapAuditEntityRepository;
    private final EntityMapper entityMapper;


    public AiEntity saveEntity(AiDetails aiDetails) {
        String aiEntityKey = KeyGenerationUtil.generateAiEntityKey(aiDetails.getAiId());
        Optional<AiEntity> optionalAiEntity = retrieveAiEntityByEntityKey(aiEntityKey);
        AiEntity entity;
        if (optionalAiEntity.isEmpty()) {
            entity = entityMapper.generateAiEntity(aiDetails);
        } else {
            entity = entityMapper.updateAiEntity(aiDetails, optionalAiEntity.get());
        }
        entity.setKey(aiEntityKey);
        saveAuditEntity(entity);
        return aiEntityRepository.save(entity);
    }

    public OuEntity saveEntity(OuDetails ouDetails) {
        String ouEntityKey = KeyGenerationUtil.generateOuEntityKey(ouDetails.getOuId());
        Optional<OuEntity> optionalOuEntity = retrieveOuEntityByEntityKey(ouEntityKey);
        OuEntity ouEntity;
        if (optionalOuEntity.isEmpty()) {
            ouEntity = entityMapper.generateOuEntity(ouDetails);
        } else {
            ouEntity = entityMapper.updateOuEntity(ouDetails, optionalOuEntity.get());
        }
        ouEntity.setKey(ouEntityKey);
        saveAuditEntity(ouEntity);
        return ouEntityRepository.save(ouEntity);
    }

    public AiOuMapEntity saveEntity(AiOuMappingDetails aiOuDetails) {
        String aiOuEntityKey = KeyGenerationUtil.generateAiOuMapEntityKey(aiOuDetails.getAiId(), aiOuDetails.getOuId());
        Optional<AiOuMapEntity> optionalAiOuMapEntity = retrieveAiOuMapEntityByEntityKey(aiOuEntityKey);
        AiOuMapEntity aiOuMapEntity;
        if (optionalAiOuMapEntity.isEmpty()) {
            aiOuMapEntity = entityMapper.generateAiOuEntity(aiOuDetails);
        } else {
            aiOuMapEntity = entityMapper.updateAiOuEntity(aiOuDetails, optionalAiOuMapEntity.get());
        }
        aiOuMapEntity.setKey(aiOuEntityKey);
        saveAuditEntity(aiOuMapEntity);
        return aiOuMappingRepository.save(aiOuMapEntity);
    }

    public void saveAuditEntity(AiEntity aiEntity) {
        String aiEntityKey = KeyGenerationUtil.generateAuditAiEntityKey(aiEntity.getId());
        AiAuditEntity auditEntity = new AiAuditEntity();
        auditEntity.setEntity(aiEntity);
        auditEntity.setKey(aiEntityKey);
        aiAuditEntityRepository.save(auditEntity);
    }

    public void saveAuditEntity(OuEntity ouEntity) {
        String entityKey = KeyGenerationUtil.generateAuditOuEntityKey(ouEntity.getId());
        OuAuditEntity auditEntity = new OuAuditEntity();
        auditEntity.setEntity(ouEntity);
        auditEntity.setKey(entityKey);
        ouAuditEntityRepository.save(auditEntity);
    }

    public void saveAuditEntity(AiOuMapEntity entity) {
        String entityKey = KeyGenerationUtil.generateAuditAiOuMapEntityKey(entity.getAiId(), entity.getOuId());
        AiOuMapAuditEntity auditEntity = new AiOuMapAuditEntity();
        auditEntity.setEntity(entity);
        auditEntity.setKey(entityKey);
        aiOuMapAuditEntityRepository.save(auditEntity);
    }

    public Optional<AiEntity> retrieveAiEntity(String aiId) {
        String aiEntityKey = KeyGenerationUtil.generateAiEntityKey(aiId);
        return aiEntityRepository.findById(aiEntityKey);
    }

    public Optional<AiEntity> retrieveAiEntityByEntityKey(String aiEntityKey) {
        return aiEntityRepository.findById(aiEntityKey);
    }

    public Optional<OuEntity> retrieveOuEntity(String ouId) {
        String ouEntityKey = KeyGenerationUtil.generateOuEntityKey(ouId);
        return ouEntityRepository.findById(ouEntityKey);
    }

    private Optional<OuEntity> retrieveOuEntityByEntityKey(String entityKey) {
        return ouEntityRepository.findById(entityKey);
    }

    public Optional<AiOuMapEntity> retrieveAiOuMapEntity(String aiId, String ouId) {
        String entityKey = KeyGenerationUtil.generateAiOuMapEntityKey(aiId, ouId);
        return aiOuMappingRepository.findById(entityKey);
    }

    public List<AiOuMapEntity> retrieveAiOuMapEntities(String aiId) {
        return aiOuMappingRepository.findAllByAiId(aiId);
    }

    private Optional<AiOuMapEntity> retrieveAiOuMapEntityByEntityKey(String entityKey) {
        return aiOuMappingRepository.findById(entityKey);
    }

    public Optional<String> getWebhookUrl(String aiId) {
        List<AiOuMapEntity> aiOuMapEntities = retrieveAiOuMapEntities(aiId);
        Optional<AiOuMapEntity> optionalEntity = aiOuMapEntities.stream()
                .findAny();
        return optionalEntity.map(AiOuMapEntity::getWebhookUrl);
    }

    public Optional<String> getWebhookUrl(String aiId, String ouId) {
        Optional<AiOuMapEntity> optionalEntity = retrieveAiOuMapEntity(aiId, ouId);
        return optionalEntity.map(AiOuMapEntity::getWebhookUrl);
    }

}
