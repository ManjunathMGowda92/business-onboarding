package org.fourstack.business.dao.repository;

import org.fourstack.business.entity.TransactionEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionEntityRepository extends MongoRepository<TransactionEntity, String> {
}
