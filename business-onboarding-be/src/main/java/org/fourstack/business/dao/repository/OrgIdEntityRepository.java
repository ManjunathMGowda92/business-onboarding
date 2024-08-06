package org.fourstack.business.dao.repository;

import org.fourstack.business.entity.OrgIdEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrgIdEntityRepository extends MongoRepository<OrgIdEntity, String> {
}
