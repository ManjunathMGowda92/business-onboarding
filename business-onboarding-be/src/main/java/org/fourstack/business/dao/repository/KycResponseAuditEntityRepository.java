package org.fourstack.business.dao.repository;

import org.fourstack.business.entity.backoffice.KycResponseAuditEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KycResponseAuditEntityRepository extends MongoRepository<KycResponseAuditEntity, String> {
}
