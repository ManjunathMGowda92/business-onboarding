package org.fourstack.business.dao.repository;

import org.fourstack.business.entity.business.OrgIdentifierEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BusinessIdentifierEntityRepository extends MongoRepository<OrgIdentifierEntity, String> {
}
