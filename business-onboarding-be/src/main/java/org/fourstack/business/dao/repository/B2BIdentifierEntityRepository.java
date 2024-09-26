package org.fourstack.business.dao.repository;

import org.fourstack.business.entity.B2BIdentifierEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface B2BIdentifierEntityRepository extends MongoRepository<B2BIdentifierEntity, String> {
}
