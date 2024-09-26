package org.fourstack.business.dao;

import org.fourstack.business.entity.KafkaMessage;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KafkaMessageRepository extends MongoRepository<KafkaMessage, String> {
}
