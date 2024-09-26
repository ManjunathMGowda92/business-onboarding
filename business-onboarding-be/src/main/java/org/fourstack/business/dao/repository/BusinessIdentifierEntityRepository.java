package org.fourstack.business.dao.repository;

import org.fourstack.business.entity.BusinessIdentifierEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BusinessIdentifierEntityRepository extends MongoRepository<BusinessIdentifierEntity, String> {
}
