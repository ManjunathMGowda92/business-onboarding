package org.fourstack.business.dao.repository;

import org.fourstack.business.entity.backoffice.KycSubTransactionEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KycSubTransactionRepository extends MongoRepository<KycSubTransactionEntity, String> {
}
