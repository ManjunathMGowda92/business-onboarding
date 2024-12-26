package org.fourstack.business.dao.repository;

import org.fourstack.business.entity.business.BusinessAuditEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BusinessAuditRepository extends MongoRepository<BusinessAuditEntity, String> {
}
