package org.fourstack.business.dao.repository;

import org.fourstack.business.entity.master.AiEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AiEntityRepository extends MongoRepository<AiEntity, String> {
}
