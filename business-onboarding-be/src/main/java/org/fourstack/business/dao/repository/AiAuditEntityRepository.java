package org.fourstack.business.dao.repository;

import org.fourstack.business.entity.AiAuditEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AiAuditEntityRepository extends MongoRepository<AiAuditEntity, String> {
}
