package org.fourstack.business.dao.repository;

import org.fourstack.business.entity.OuEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OuEntityRepository extends MongoRepository<OuEntity, String> {
}
