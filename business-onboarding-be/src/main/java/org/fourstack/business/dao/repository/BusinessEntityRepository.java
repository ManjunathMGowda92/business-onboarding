package org.fourstack.business.dao.repository;

import org.fourstack.business.entity.BusinessEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BusinessEntityRepository extends MongoRepository<BusinessEntity, String> {
    List<BusinessEntity> findAllByInstituteLeiValue(String leiValue);
}
