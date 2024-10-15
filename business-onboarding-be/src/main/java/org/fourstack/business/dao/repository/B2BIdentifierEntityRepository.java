package org.fourstack.business.dao.repository;

import org.fourstack.business.entity.B2BIdentifierEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface B2BIdentifierEntityRepository extends MongoRepository<B2BIdentifierEntity, String> {
    List<B2BIdentifierEntity> findAllByB2bIdValue(Collection<String> b2bIds);
}
