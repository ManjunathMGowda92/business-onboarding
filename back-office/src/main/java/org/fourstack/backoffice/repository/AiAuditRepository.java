package org.fourstack.backoffice.repository;

import org.fourstack.backoffice.entity.AiAuditEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AiAuditRepository extends MongoRepository<AiAuditEntity, String> {
}
