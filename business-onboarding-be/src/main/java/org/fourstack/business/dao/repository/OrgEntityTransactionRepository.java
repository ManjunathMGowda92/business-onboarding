package org.fourstack.business.dao.repository;

import org.fourstack.business.entity.OrgIdTransactionEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrgEntityTransactionRepository extends MongoRepository<OrgIdTransactionEntity, String> {
}
