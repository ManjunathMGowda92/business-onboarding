package org.fourstack.backoffice.repository;

import org.fourstack.backoffice.entity.AiOuMappingEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AiOuMappingRepository extends MongoRepository<AiOuMappingEntity, String> {
    List<AiOuMappingEntity> findAllByAiId(String aiId);
}
