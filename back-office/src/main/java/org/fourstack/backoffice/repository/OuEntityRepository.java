package org.fourstack.backoffice.repository;

import org.fourstack.backoffice.entity.OuEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OuEntityRepository extends MongoRepository<OuEntity, String> {
}
