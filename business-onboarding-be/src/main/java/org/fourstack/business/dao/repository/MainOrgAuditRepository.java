package org.fourstack.business.dao.repository;

import org.fourstack.business.entity.business.MainOrgAuditEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MainOrgAuditRepository extends MongoRepository<MainOrgAuditEntity, String> {
}
