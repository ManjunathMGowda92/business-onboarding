package org.fourstack.backoffice.repository;

import org.fourstack.backoffice.entity.OperationUnitEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OuEntityRepository extends MongoRepository<OperationUnitEntity, String> {
}
