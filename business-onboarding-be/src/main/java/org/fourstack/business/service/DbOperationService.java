package org.fourstack.business.service;

import lombok.RequiredArgsConstructor;
import org.fourstack.business.dao.service.B2BIdDataService;
import org.fourstack.business.dao.service.BusinessEntityDataService;
import org.fourstack.business.dao.service.BusinessIdentifierDataService;
import org.fourstack.business.dao.service.MasterDataService;
import org.fourstack.business.dao.service.OrgEntityDataService;
import org.fourstack.business.entity.AiEntity;
import org.fourstack.business.entity.AiOuMapEntity;
import org.fourstack.business.entity.B2BIdentifierEntity;
import org.fourstack.business.entity.BusinessEntity;
import org.fourstack.business.entity.BusinessIdentifierEntity;
import org.fourstack.business.entity.OrgIdEntity;
import org.fourstack.business.entity.OuEntity;
import org.fourstack.business.enums.BooleanStatus;
import org.fourstack.business.model.CheckInstitute;
import org.fourstack.business.model.CheckInstituteResponse;
import org.fourstack.business.model.Institute;
import org.fourstack.business.model.InstituteInfo;
import org.fourstack.business.utils.BusinessUtil;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor_ = @Lazy)
public class DbOperationService {
    private final MasterDataService masterDataService;
    private final OrgEntityDataService orgEntityDataService;
    private final BusinessEntityDataService businessDataService;
    private final B2BIdDataService b2BIdDataService;
    private final BusinessIdentifierDataService identifierDataService;

    public Optional<AiEntity> retrieveAiEntity(String aiId) {
        return masterDataService.retrieveAiEntity(aiId);
    }

    public Optional<OuEntity> retrieveOuEntity(String ouId) {
        return masterDataService.retrieveOuEntity(ouId);
    }

    public Optional<AiOuMapEntity> retrieveAiOuMapEntity(String aiId, String ouId) {
        return masterDataService.retrieveAiOuMapEntity(aiId, ouId);
    }

    public boolean checkIsBusinessExist(String leiValue) {
        List<BusinessEntity> businessEntities = businessDataService.retrieveBusinessEntities(leiValue);
        return !businessEntities.isEmpty();
    }

    public Optional<OrgIdEntity> retrieveOrgIdEntity(String orgId) {
        return orgEntityDataService.retrieveOrgIdEntity(orgId);
    }

    public Optional<BusinessIdentifierEntity> retrieveBusinessIdentifierEntity(String identifierType, String identifierValue) {
        return identifierDataService.retrieveIdentifierEntity(identifierType, identifierValue);
    }

    public Optional<B2BIdentifierEntity> retrieveB2BIdentifierEntity(String b2bId) {
        return b2BIdDataService.retrieveB2BId(b2bId);
    }

    public Optional<BusinessEntity> retrieveBusinessEntity(String businessKey) {
        return businessDataService.retrieveBusiness(businessKey);
    }

    public Map<String, BusinessEntity> retrieveBusinessEntities(String leiValue) {
        List<BusinessEntity> businessEntities = businessDataService.retrieveBusinessEntities(leiValue);
        if (BusinessUtil.isCollectionNotNullOrEmpty(businessEntities)) {
            return businessEntities.stream()
                    .collect(Collectors.toMap(BusinessEntity::getKey, value -> value, (newValue, olValue) -> newValue));
        } else {
            return new HashMap<>();
        }
    }

    public CheckInstituteResponse generateCheckInstituteResponse(CheckInstitute checkInstitute, boolean isBusinessExist,
                                                                 boolean isMultipleBusinessAllowed) {
        CheckInstituteResponse instituteResponse = new CheckInstituteResponse();
        List<InstituteInfo> instituteInfoList = new ArrayList<>();
        if (isBusinessExist) {
            Map<String, BusinessEntity> businessEntityMap = retrieveBusinessEntities(checkInstitute.getValue());
            for (Map.Entry<String, BusinessEntity> entry : businessEntityMap.entrySet()) {
                Institute institute = entry.getValue().getInstitute();
                instituteInfoList.add(getInstituteInfo(isMultipleBusinessAllowed, institute));
            }
            instituteResponse.setInstitutes(instituteInfoList);
        } else {
            InstituteInfo instituteInfo = getInstituteInfo(BooleanStatus.NO,
                    isMultipleBusinessAllowed ? BooleanStatus.YES : BooleanStatus.NO,
                    checkInstitute.getRegisteredName(), Collections.emptyList());
            instituteResponse.setInstitutes(List.of(instituteInfo));
        }
        return instituteResponse;
    }

    private InstituteInfo getInstituteInfo(boolean isMultipleBusinessAllowed, Institute institute) {
        Optional<OrgIdEntity> orgIdEntity = retrieveOrgIdEntity(institute.getObjectId());
        if (orgIdEntity.isPresent()) {
            Set<String> b2BIds = BusinessUtil.extractAllB2BIds(orgIdEntity.get());
            return getInstituteInfo(BooleanStatus.YES, isMultipleBusinessAllowed ? BooleanStatus.YES : BooleanStatus.NO,
                    institute.getName(), b2BIds);
        } else {
            return getInstituteInfo(BooleanStatus.YES, isMultipleBusinessAllowed ? BooleanStatus.YES : BooleanStatus.NO,
                    institute.getName(), List.of(institute.getDefaultB2bId()));
        }
    }

    private InstituteInfo getInstituteInfo(BooleanStatus businessExist, BooleanStatus multipleOrgAllowed,
                                           String businessName, Collection<String> b2bIds) {
        InstituteInfo instituteInfo = new InstituteInfo();
        instituteInfo.setIsBusinessExist(businessExist);
        instituteInfo.setIsMultipleOrgAllowed(multipleOrgAllowed);
        instituteInfo.setBusinessName(businessName);
        instituteInfo.setB2bIds(b2bIds);
        return instituteInfo;
    }
}
