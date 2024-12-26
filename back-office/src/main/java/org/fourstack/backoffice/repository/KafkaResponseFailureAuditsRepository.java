package org.fourstack.backoffice.repository;

import org.fourstack.backoffice.entity.kyc.KafkaResponseFailureAudits;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KafkaResponseFailureAuditsRepository extends MongoRepository<KafkaResponseFailureAudits, String> {
}
