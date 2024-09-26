package org.fourstack.business.dao.repository;

import org.fourstack.business.entity.OuAuditEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OuAuditEntityRepository extends MongoRepository<OuAuditEntity, String> {
}
