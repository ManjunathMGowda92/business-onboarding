package org.fourstack.backoffice.repository.kyc;

import org.fourstack.backoffice.entity.kyc.KycResponseAudit;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface KycResponseAuditRepository extends MongoRepository<KycResponseAudit, String> {
}
