package org.fourstack.business.dao.repository;

import org.fourstack.business.entity.AiOuMapEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AiOuMappingRepository extends MongoRepository<AiOuMapEntity, String> {
}
