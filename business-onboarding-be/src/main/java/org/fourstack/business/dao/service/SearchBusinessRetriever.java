package org.fourstack.business.dao.service;

import lombok.RequiredArgsConstructor;
import org.fourstack.business.constants.BusinessConstants;
import org.fourstack.business.constants.ValidationConstants;
import org.fourstack.business.entity.B2BIdEntity;
import org.fourstack.business.entity.B2BIdentifierEntity;
import org.fourstack.business.entity.BusinessEntity;
import org.fourstack.business.entity.BusinessIdentifierEntity;
import org.fourstack.business.entity.MainOrgIdEntity;
import org.fourstack.business.enums.AddressType;
import org.fourstack.business.enums.EntityStatus;
import org.fourstack.business.enums.ErrorScenarioCode;
import org.fourstack.business.enums.PrivacyType;
import org.fourstack.business.model.Address;
import org.fourstack.business.model.BusinessIdentifier;
import org.fourstack.business.model.EntityInfo;
import org.fourstack.business.model.Institute;
import org.fourstack.business.model.ResponseB2BId;
import org.fourstack.business.model.SearchCriteria;
import org.fourstack.business.model.SearchRequest;
import org.fourstack.business.service.DbOperationService;
import org.fourstack.business.utils.BusinessUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor(onConstructor_ = @Lazy)
public class SearchBusinessRetriever {
    private static final Logger logger = LoggerFactory.getLogger(SearchBusinessRetriever.class);
    private final DbOperationService dbOperationService;

    public List<EntityInfo> retrieveSearchResults(SearchRequest request) {
        logger.info("Retrieving the results for Search Request");
        List<SearchCriteria> criteriaList = request.getCriteria();
        List<EntityInfo> entityInfoResults = new ArrayList<>();
        if (BusinessUtil.isCollectionNotNullOrEmpty(criteriaList)) {
            for (SearchCriteria criteria : criteriaList) {
                entityInfoResults.addAll(retrieveBySearchCriteria(criteria));
            }
        }
        return entityInfoResults;
    }

    private List<EntityInfo> retrieveBySearchCriteria(SearchCriteria searchCriteria) {
        String searchParameter = searchCriteria.getSearchParameter();
        switch (searchParameter) {
            case BusinessConstants.PAN -> {
                return searchByPan(searchCriteria.getValue());
            }
            case BusinessConstants.B2B_ID -> {
                return List.of(searchByB2bId(searchCriteria.getValue()));
            }
            case BusinessConstants.GSTIN, BusinessConstants.TAN, BusinessConstants.FSSAI,
                    BusinessConstants.SHOP_ESTABLISHMENT_NUM, BusinessConstants.UDYAM -> {
                return List.of(searchByIdentifier(searchParameter, searchCriteria.getValue()));
            }
            default -> throw BusinessUtil.generateValidationException("Unknown Search Parameter : " + searchParameter,
                    ValidationConstants.SEARCH_PARAMETER, ErrorScenarioCode.BU_ONB_0014);
        }
    }

    private EntityInfo searchByIdentifier(String identifierType, String identifierValue) {
        logger.info("Retrieving the search results by Identifiers - Parameter : {} - value : {}", identifierType, identifierValue);
        Optional<BusinessIdentifierEntity> optionalBusinessIdentifierEntity =
                dbOperationService.retrieveBusinessIdentifierEntity(identifierType, identifierValue);
        if (optionalBusinessIdentifierEntity.isPresent()) {
            BusinessIdentifierEntity identifierEntity = optionalBusinessIdentifierEntity.get();
            Optional<MainOrgIdEntity> optionalOrgIdEntity = dbOperationService.retrieveOrgIdEntity(identifierEntity.getOrgId());
            if (optionalOrgIdEntity.isPresent()) {
                MainOrgIdEntity orgIdEntity = optionalOrgIdEntity.get();
                Optional<BusinessEntity> businessEntity =
                        dbOperationService.retrieveBusinessEntity(orgIdEntity.getBusinessKey());
                if (businessEntity.isPresent()) {
                    EntityInfo entityInfo = new EntityInfo();
                    addBusinessValuesToEntityInfo(entityInfo, businessEntity.get());
                    entityInfo.setStatus(orgIdEntity.getStatus());
                    Set<String> b2bIds = BusinessUtil.extractAllB2BIds(orgIdEntity);
                    List<ResponseB2BId> responseB2BIdList = constructResponseB2BInfo(b2bIds, identifierType, identifierValue);
                    entityInfo.setB2BIdInfoList(responseB2BIdList);
                    return entityInfo;
                }
            }
        }
        throw BusinessUtil.generateValidationException("No Business found for : [" + identifierType + ", " + identifierValue + "]",
                ValidationConstants.SEARCH_CRITERIA_VALUE, ErrorScenarioCode.BU_ONB_0016);
    }


    private EntityInfo searchByB2bId(String b2bId) {
        logger.info("Retrieving the search results by parameter : B2BID - value :{}", b2bId);
        Optional<B2BIdentifierEntity> optionalIdentifierEntity = dbOperationService.retrieveB2BIdentifierEntity(b2bId);
        if (optionalIdentifierEntity.isPresent()) {
            B2BIdentifierEntity identifierEntity = optionalIdentifierEntity.get();
            String privacyType = identifierEntity.getB2BId().getPrivacyType();
            if (PrivacyType.PRIVATE.name().equals(privacyType)) {
                throw BusinessUtil.generateValidationException("Search B2B ID value : " + b2bId + " is private",
                        ValidationConstants.SEARCH_CRITERIA_VALUE, ErrorScenarioCode.BU_ONB_0015);
            }
            Optional<MainOrgIdEntity> optionalOrgIdEntity = dbOperationService.retrieveOrgIdEntity(identifierEntity.getOrgId());
            if (optionalOrgIdEntity.isPresent()) {
                Optional<BusinessEntity> businessEntity =
                        dbOperationService.retrieveBusinessEntity(optionalOrgIdEntity.get().getBusinessKey());
                if (businessEntity.isPresent()) {
                    EntityInfo entityInfo = constructEntityInfo(businessEntity.get(), identifierEntity);
                    entityInfo.setStatus(optionalOrgIdEntity.get().getStatus());
                    return entityInfo;
                }
            }
        }
        throw BusinessUtil.generateValidationException("No Business found for : [ B2BID, " + b2bId + "]",
                ValidationConstants.SEARCH_CRITERIA_VALUE, ErrorScenarioCode.BU_ONB_0016);
    }

    private EntityInfo constructEntityInfo(BusinessEntity businessEntity, B2BIdentifierEntity identifierEntity) {
        EntityInfo entityInfo = new EntityInfo();
        addBusinessValuesToEntityInfo(entityInfo, businessEntity);
        entityInfo.setB2BIdInfoList(List.of(constructResponseB2BInfo(identifierEntity)));
        return entityInfo;
    }

    private ResponseB2BId constructResponseB2BInfo(B2BIdentifierEntity identifierEntity) {
        ResponseB2BId responseB2BId = new ResponseB2BId();
        responseB2BId.setB2bId(identifierEntity.getB2bIdValue());
        B2BIdEntity b2BId = identifierEntity.getB2BId();
        if (BusinessUtil.isNotNull(b2BId)) {
            responseB2BId.setDescription(b2BId.getDescription());
            responseB2BId.setBusinessIdentifier(b2BId.getBusinessIdentifier());
        }
        responseB2BId.setStatus(EntityStatus.ACTIVE == identifierEntity.getStatus()
                ? EntityStatus.ACTIVE : EntityStatus.INACTIVE);
        return responseB2BId;
    }

    private List<ResponseB2BId> constructResponseB2BInfo(Set<String> b2bIds) {
        List<ResponseB2BId> responseB2BIdList = new ArrayList<>();
        for (String b2bId : b2bIds) {
            Optional<B2BIdentifierEntity> optionalB2BEntity = dbOperationService.retrieveB2BIdentifierEntity(b2bId);
            optionalB2BEntity.ifPresent(entity -> responseB2BIdList.add(constructResponseB2BInfo(entity)));
        }
        return responseB2BIdList;
    }

    private List<ResponseB2BId> constructResponseB2BInfo(Set<String> b2bIds, String identifierType, String identifierValue) {
        List<ResponseB2BId> responseB2BIdList = new ArrayList<>();
        for (String b2bId : b2bIds) {
            Optional<B2BIdentifierEntity> optionalB2BEntity = dbOperationService.retrieveB2BIdentifierEntity(b2bId);
            if (optionalB2BEntity.isPresent()) {
                B2BIdentifierEntity b2BIdentifierEntity = optionalB2BEntity.get();
                if (BusinessUtil.isNotNull(b2BIdentifierEntity.getB2BId())
                        && BusinessUtil.isNotNull(b2BIdentifierEntity.getB2BId().getBusinessIdentifier())) {
                    BusinessIdentifier businessIdentifier = b2BIdentifierEntity.getB2BId().getBusinessIdentifier();
                    if (identifierType.equals(businessIdentifier.getDocumentName())
                            && identifierValue.equals(businessIdentifier.getValue())) {
                        responseB2BIdList.add(constructResponseB2BInfo(b2BIdentifierEntity));
                    }
                }
            }
        }
        return responseB2BIdList;
    }

    private void addBusinessValuesToEntityInfo(EntityInfo entityInfo, BusinessEntity businessEntity) {
        Institute institute = businessEntity.getInstitute();
        entityInfo.setBusinessName(institute.getName());
        entityInfo.setMccCode(institute.getMccCode());
        entityInfo.setObjectId(institute.getObjectId());

        List<Address> addresses = institute.getAddresses();
        if (BusinessUtil.isCollectionNotNullOrEmpty(addresses)) {
            Optional<Address> optionalAddress = addresses.stream()
                    .filter(address -> AddressType.REGISTERED.getType().equals(address.getType()))
                    .findFirst();
            if (optionalAddress.isPresent()) {
                entityInfo.setAddress(optionalAddress.get());
            } else {
                entityInfo.setAddress(addresses.getFirst());
            }
        }
    }

    private List<EntityInfo> searchByPan(String panValue) {
        logger.info("Retrieving the results with search parameter - PAN : value - {}", panValue);
        Map<String, BusinessEntity> businessEntityMap = dbOperationService.retrieveBusinessEntities(panValue);
        if (businessEntityMap.isEmpty()) {
            throw BusinessUtil.generateValidationException("No Business found for : [ PAN, " + panValue + "]",
                    ValidationConstants.SEARCH_CRITERIA_VALUE, ErrorScenarioCode.BU_ONB_0016);
        }
        List<EntityInfo> entityInfoList = new ArrayList<>();
        for (Map.Entry<String, BusinessEntity> entry : businessEntityMap.entrySet()) {
            BusinessEntity businessEntity = entry.getValue();
            String objectId = businessEntity.getInstitute().getObjectId();
            Optional<MainOrgIdEntity> orgIdEntity = dbOperationService.retrieveOrgIdEntity(objectId);
            if (orgIdEntity.isPresent()) {
                EntityInfo entityInfo = new EntityInfo();
                addBusinessValuesToEntityInfo(entityInfo, businessEntity);
                entityInfo.setStatus(orgIdEntity.get().getStatus());
                Set<String> b2bIds = BusinessUtil.extractAllB2BIds(orgIdEntity.get());
                List<ResponseB2BId> responseB2BIdList = constructResponseB2BInfo(b2bIds);
                entityInfo.setB2BIdInfoList(responseB2BIdList);
                entityInfoList.add(entityInfo);
            }
        }
        return entityInfoList;
    }
}
