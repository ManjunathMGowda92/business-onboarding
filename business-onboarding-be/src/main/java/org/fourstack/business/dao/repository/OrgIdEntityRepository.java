package org.fourstack.business.dao.repository;

import org.fourstack.business.entity.MainOrgIdEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrgIdEntityRepository extends MongoRepository<MainOrgIdEntity, String> {
}
