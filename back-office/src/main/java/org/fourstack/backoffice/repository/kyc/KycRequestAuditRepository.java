package org.fourstack.backoffice.repository.kyc;

import org.fourstack.backoffice.entity.kyc.KycRequestAuditEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KycRequestAuditRepository extends MongoRepository<KycRequestAuditEntity, String> {
}
