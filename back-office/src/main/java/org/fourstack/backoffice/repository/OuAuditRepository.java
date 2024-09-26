package org.fourstack.backoffice.repository;

import org.fourstack.backoffice.entity.OuAuditEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OuAuditRepository extends MongoRepository<OuAuditEntity, String> {
}
