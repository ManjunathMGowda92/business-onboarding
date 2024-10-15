package org.fourstack.business.dao.repository;

import org.fourstack.business.entity.AiOuMapEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AiOuMappingRepository extends MongoRepository<AiOuMapEntity, String> {
    List<AiOuMapEntity> findAllByAiId(String aiId);
}
