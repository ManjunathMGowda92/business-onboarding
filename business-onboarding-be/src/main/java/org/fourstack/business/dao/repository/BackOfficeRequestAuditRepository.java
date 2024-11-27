package org.fourstack.business.dao.repository;

import org.fourstack.business.entity.BackOfficeRequestAudit;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BackOfficeRequestAuditRepository extends MongoRepository<BackOfficeRequestAudit, String> {
}
