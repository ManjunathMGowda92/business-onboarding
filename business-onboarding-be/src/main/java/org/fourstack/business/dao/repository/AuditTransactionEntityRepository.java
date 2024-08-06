package org.fourstack.business.dao.repository;

import org.fourstack.business.entity.AuditTransactionEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuditTransactionEntityRepository extends MongoRepository<AuditTransactionEntity, String> {
}
