package org.fourstack.business.dao.repository;

import org.fourstack.business.entity.business.AiOrgMapAuditEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AiOrgMapAuditRepository extends MongoRepository<AiOrgMapAuditEntity, String> {
}
