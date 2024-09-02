package org.fourstack.business.dao.repository;

import org.fourstack.business.entity.AiOrgMapEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AiOrgMapEntityRepository extends MongoRepository<AiOrgMapEntity, String> {
}
