package org.fourstack.backoffice.repository;

import org.fourstack.backoffice.entity.AiOuMapAuditEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AiOuAuditRepository extends MongoRepository<AiOuMapAuditEntity, String> {
}
