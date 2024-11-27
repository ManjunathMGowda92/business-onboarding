package org.fourstack.business.dao.repository;

import org.fourstack.business.entity.KycTransactionEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KycTransactionRepository extends MongoRepository<KycTransactionEntity, String> {
}
