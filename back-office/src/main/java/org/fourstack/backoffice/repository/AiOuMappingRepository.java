package org.fourstack.backoffice.repository;

import org.fourstack.backoffice.entity.AiOuMappingEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AiOuMappingRepository extends MongoRepository<AiOuMappingEntity, String> {
}
