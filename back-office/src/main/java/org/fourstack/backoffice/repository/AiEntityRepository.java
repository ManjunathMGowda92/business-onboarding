package org.fourstack.backoffice.repository;

import org.fourstack.backoffice.entity.AiEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AiEntityRepository extends MongoRepository<AiEntity, String> {
}
