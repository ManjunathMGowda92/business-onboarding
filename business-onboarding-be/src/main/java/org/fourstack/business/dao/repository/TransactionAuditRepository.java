package org.fourstack.business.dao.repository;

import org.fourstack.business.entity.common.TransactionAuditEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionAuditRepository extends MongoRepository<TransactionAuditEntity, String> {
}
