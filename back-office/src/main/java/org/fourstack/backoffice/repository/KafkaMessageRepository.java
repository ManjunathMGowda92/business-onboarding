package org.fourstack.backoffice.repository;

import org.fourstack.backoffice.entity.KafkaMessage;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KafkaMessageRepository extends MongoRepository<KafkaMessage, String> {
}
