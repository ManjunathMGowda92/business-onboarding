package org.fourstack.backoffice.repository.kyc;

import org.fourstack.backoffice.entity.kyc.KycRequestEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KycRequestRepository extends MongoRepository<KycRequestEntity, String> {
}
