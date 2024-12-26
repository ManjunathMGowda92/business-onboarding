package org.fourstack.business.dao.repository;

import org.fourstack.business.entity.common.TransactionEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionEntityRepository extends MongoRepository<TransactionEntity, String> {
}
