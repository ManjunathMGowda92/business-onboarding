package org.fourstack.business.validator;

import lombok.RequiredArgsConstructor;
import org.fourstack.business.config.MultipleBusinessAllowedConfig;
import org.fourstack.business.constants.ValidationConstants;
import org.fourstack.business.entity.AiEntity;
import org.fourstack.business.entity.AiOuMapEntity;
import org.fourstack.business.entity.B2BIdentifierEntity;
import org.fourstack.business.entity.BusinessIdentifierEntity;
import org.fourstack.business.entity.Entity;
import org.fourstack.business.entity.MainOrgIdEntity;
import org.fourstack.business.entity.OuEntity;
import org.fourstack.business.enums.EntityStatus;
import org.fourstack.business.enums.ErrorScenarioCode;
import org.fourstack.business.mapper.ResponseMapper;
import org.fourstack.business.model.CheckB2BIdRequest;
import org.fourstack.business.model.backoffice.AiOuMappingDetails;
import org.fourstack.business.model.B2BId;
import org.fourstack.business.model.B2BIdRegisterRequest;
import org.fourstack.business.model.BusinessIdentifier;
import org.fourstack.business.model.BusinessRegisterRequest;
import org.fourstack.business.model.CheckBusinessRequest;
import org.fourstack.business.model.CheckInstitute;
import org.fourstack.business.model.CheckInstituteResponse;
import org.fourstack.business.model.Head;
import org.fourstack.business.model.Institute;
import org.fourstack.business.model.SearchBusinessRequest;
import org.fourstack.business.service.DbOperationService;
import org.fourstack.business.utils.BusinessUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor(onConstructor_ = @Lazy)
public class BusinessValidator {
    private static final Logger logger = LoggerFactory.getLogger(BusinessValidator.class);
    private final DbOperationService dbOperationService;
    private final MultipleBusinessAllowedConfig multipleBusinessAllowedConfig;
    private final ResponseMapper responseMapper;


    public void businessRegisterValidations(BusinessRegisterRequest request) {
        validateAiAndOuEntities(request.getCommonData().getHead());
        Institute institute = request.getInstitute();
        checkOrgIdEntityExistence(institute.getObjectId(), ValidationConstants.INSTITUTE_ORG_ID,
                ErrorScenarioCode.BU_ONB_0003);
        checkMultipleBusinessAllowedForBusiness(institute.getLei().getType(), institute.getLei().getValue());
        validateB2BIdentifierAvailability(institute.getDefaultB2bId(), ValidationConstants.DEFAULT_B2B_ID,
                ErrorScenarioCode.BU_ONB_0004);
        validateBusinessIdentifierExistence(institute.getPrimaryIdentifier(),
                ValidationConstants.INSTITUTE_PRIMARY_IDENTIFIER_VALUE, ErrorScenarioCode.BU_ONB_0005);
        List<BusinessIdentifier> otherIdentifiers = institute.getOtherIdentifiers();
        if (BusinessUtil.isCollectionNotNullOrEmpty(otherIdentifiers)) {
            otherIdentifiers.forEach(identifier -> validateBusinessIdentifierExistence(identifier,
                    ValidationConstants.INSTITUTE_OTHER_IDENTIFIER_VALUE, ErrorScenarioCode.BU_ONB_0005));
        }
    }

    public void b2bBusinessRegisterValidations(B2BIdRegisterRequest request) {
        validateAiAndOuEntities(request.getCommonData().getHead());
        B2BIdentifierEntity b2BIdentifierEntity =
                validateAndRetrieveB2BIdentifierEntity(request.getOnboardingB2BIds().getRequesterB2BId(),
                        ValidationConstants.REQUESTER_B2B_ID, ErrorScenarioCode.BU_ONB_0006);
        validateEntityStatus(b2BIdentifierEntity, EntityStatus.ACTIVE, ValidationConstants.REQUESTER_B2B_ID,
                ErrorScenarioCode.BU_ONB_0007);
        checkIsB2bIdAssociatedToAiId(b2BIdentifierEntity, request.getCommonData().getHead().getAiId(),
                ValidationConstants.REQUESTER_B2B_ID, ErrorScenarioCode.BU_ONB_0011);
        MainOrgIdEntity orgIdEntity = validateAndRetrieveOrgIdEntity(b2BIdentifierEntity.getOrgId(),
                ValidationConstants.REQUESTER_B2B_ID, ErrorScenarioCode.BU_ONB_0012);
        validateEntityStatus(orgIdEntity, EntityStatus.ACTIVE, ValidationConstants.REQUESTER_B2B_ID,
                ErrorScenarioCode.BU_ONB_0013);

        List<B2BId> b2BIdList = request.getRegB2BIds().getIds();
        for (B2BId b2BId : b2BIdList) {
            validateB2BIdentifierAvailability(b2BId.getValue(), ValidationConstants.REG_B2B_ID_VALUE,
                    ErrorScenarioCode.BU_ONB_0008);
            BusinessIdentifierEntity businessIdentifierEntity =
                    validateAndRetrieveBusinessIdentifier(b2BId.getBusinessIdentifier(),
                            ValidationConstants.REG_B2B_ID_IDENTIFIER, ErrorScenarioCode.BU_ONB_0009);
            validateIsIdentifierAssociatedToOrgEntity(businessIdentifierEntity, orgIdEntity,
                    ValidationConstants.REG_B2B_ID_IDENTIFIER, ErrorScenarioCode.BU_ONB_0010);
        }
    }

    public CheckInstituteResponse checkBusinessValidations(CheckBusinessRequest request) {
        logger.info("Business Validations for CheckBusinessRequest");
        validateAiAndOuEntities(request.getCommonData().getHead());
        CheckInstitute checkInstitute = request.getCheckInstitute();

        boolean businessExist = dbOperationService.checkIsBusinessExist(checkInstitute.getValue());
        boolean isMultipleBusinessAllowed = checkMultipleBusinessAllowedForLeiType(checkInstitute.getType());
        return responseMapper.generateCheckInstituteResponse(request.getCheckInstitute(), businessExist, isMultipleBusinessAllowed);
    }

    public void searchBusinessValidations(SearchBusinessRequest request) {
        logger.info("Search Business Validations : START");
        validateAiAndOuEntities(request.getCommonData().getHead());
        B2BIdentifierEntity b2BIdentifierEntity =
                validateAndRetrieveB2BIdentifierEntity(request.getOnboardingB2BIds().getRequesterB2BId(),
                        ValidationConstants.REQUESTER_B2B_ID, ErrorScenarioCode.BU_ONB_0006);
        validateEntityStatus(b2BIdentifierEntity, EntityStatus.ACTIVE, ValidationConstants.REQUESTER_B2B_ID,
                ErrorScenarioCode.BU_ONB_0007);
        checkIsB2bIdAssociatedToAiId(b2BIdentifierEntity, request.getCommonData().getHead().getAiId(),
                ValidationConstants.REQUESTER_B2B_ID, ErrorScenarioCode.BU_ONB_0011);
    }

    private void validateIsIdentifierAssociatedToOrgEntity(BusinessIdentifierEntity identifierEntity, MainOrgIdEntity orgIdEntity,
                                                           String fieldName, ErrorScenarioCode errorScenarioCode) {
        Set<String> orgIdentifiers = BusinessUtil.extractAllIdentifiers(orgIdEntity);
        BusinessIdentifier identifier = identifierEntity.getIdentifier();
        if (BusinessUtil.isNotNull(identifier)) {
            String businessIdentifier = identifier.getDocumentName() + ":" + identifier.getValue();
            if (!orgIdentifiers.contains(businessIdentifier)) {
                generateValidationException("Business Identifier not associated to Org Entity", fieldName, errorScenarioCode);
            }
        } else {
            generateValidationException("Business Identifier is null", fieldName, errorScenarioCode);
        }
    }

    private MainOrgIdEntity validateAndRetrieveOrgIdEntity(String orgId, String fieldName,
                                                           ErrorScenarioCode errorScenarioCode) {
        Optional<MainOrgIdEntity> orgIdEntity = dbOperationService.retrieveOrgIdEntity(orgId);
        if (orgIdEntity.isEmpty()) {
            generateValidationException("OrgIdEntity not exist for objectId : " + orgId, fieldName, errorScenarioCode);
        }
        return orgIdEntity.get();
    }

    private void validateEntityStatus(Entity entity, EntityStatus expectedStatus, String fieldName,
                                      ErrorScenarioCode scenarioCode) {
        if (expectedStatus != entity.getStatus()) {
            generateValidationException("Entity is not in requested status : " + expectedStatus,
                    fieldName, scenarioCode);
        }
    }

    private void checkIsB2bIdAssociatedToAiId(B2BIdentifierEntity b2BIdentifierEntity, String aiId,
                                              String fieldName, ErrorScenarioCode errorScenarioCode) {
        String primaryAiId = b2BIdentifierEntity.getPrimaryAiId();
        boolean isMatched = primaryAiId.equals(aiId);
        if (!isMatched) {
            Set<String> secondaryAiIds = b2BIdentifierEntity.getSecondaryAiIds();
            if (!secondaryAiIds.contains(aiId)) {
                generateValidationException("B2B Id : " + b2BIdentifierEntity.getB2bIdValue() + "not associated to AI Id : "
                        + aiId, fieldName, errorScenarioCode);
            }
        }
    }

    private void validateBusinessIdentifierExistence(BusinessIdentifier businessIdentifier, String fieldName,
                                                     ErrorScenarioCode scenarioCode) {
        checkBusinessIdentifierExistence(businessIdentifier.getDocumentName(), businessIdentifier.getValue(),
                fieldName, scenarioCode);
    }

    private void checkBusinessIdentifierExistence(String identifierType, String identifierValue,
                                                  String fieldName, ErrorScenarioCode errorScenarioCode) {
        Optional<BusinessIdentifierEntity> businessIdentifierEntity =
                dbOperationService.retrieveBusinessIdentifierEntity(identifierType, identifierValue);
        if (businessIdentifierEntity.isPresent()) {
            generateValidationException("BusinessIdentifierEntity already exist for the identifier type: "
                    + identifierType + ", value : " + identifierValue, fieldName, errorScenarioCode);
        }
    }

    private BusinessIdentifierEntity validateAndRetrieveBusinessIdentifier(BusinessIdentifier businessIdentifier,
                                                                           String fieldName, ErrorScenarioCode errorScenarioCode) {
        Optional<BusinessIdentifierEntity> businessIdentifierEntity =
                dbOperationService.retrieveBusinessIdentifierEntity(businessIdentifier.getDocumentName(), businessIdentifier.getValue());
        if (businessIdentifierEntity.isEmpty()) {
            generateValidationException("BusinessIdentifierEntity not exist for the identifier type: "
                    + businessIdentifier.getDocumentName() + ", value : " + businessIdentifier.getValue(), fieldName, errorScenarioCode);
        }
        return businessIdentifierEntity.get();
    }

    private void validateB2BIdentifierAvailability(String b2bId, String fieldName, ErrorScenarioCode errorScenarioCode) {
        Optional<B2BIdentifierEntity> b2BIdentifierEntity = dbOperationService.retrieveB2BIdentifierEntity(b2bId);
        if (b2BIdentifierEntity.isPresent()) {
            generateValidationException("B2BIdentifierEntity already exist for the b2bId : " + b2bId, fieldName, errorScenarioCode);
        }
    }

    private B2BIdentifierEntity validateAndRetrieveB2BIdentifierEntity(String b2bId, String fieldName, ErrorScenarioCode errorScenarioCode) {
        Optional<B2BIdentifierEntity> b2BIdentifierEntity = dbOperationService.retrieveB2BIdentifierEntity(b2bId);
        if (b2BIdentifierEntity.isEmpty()) {
            generateValidationException("B2BIdentifierEntity not exist for the b2bId : " + b2bId, fieldName, errorScenarioCode);
        }
        return b2BIdentifierEntity.get();
    }

    private void checkOrgIdEntityExistence(String orgId, String fieldName, ErrorScenarioCode errorScenarioCode) {
        Optional<MainOrgIdEntity> orgIdEntity = dbOperationService.retrieveOrgIdEntity(orgId);
        if (orgIdEntity.isPresent()) {
            generateValidationException("OrgIdEntity already exist for the orgId : " + orgId, fieldName, errorScenarioCode);
        }
    }

    private void checkMultipleBusinessAllowedForBusiness(String leiType, String leiValue) {
        boolean businessExist = dbOperationService.checkIsBusinessExist(leiValue);
        if (businessExist) {
            boolean multipleBusinessAllowedForLeiType = checkMultipleBusinessAllowedForLeiType(leiType);
            if (!multipleBusinessAllowedForLeiType) {
                generateValidationException("Business Entity already Exist and multiple Business not " +
                                "allowed for lei: " + leiValue + " and type : " + leiType,
                        ValidationConstants.INSTITUTE_LEI_VALUE, ErrorScenarioCode.BU_ONB_0001);
            }
        }
    }

    private boolean checkMultipleBusinessAllowedForLeiType(String leiType) {
        Map<String, String> multipleBusinessConfigMap = multipleBusinessAllowedConfig.getMultipleBusinessConfig();
        if (BusinessUtil.isMapNotNullOrEmpty(multipleBusinessConfigMap)) {
            return multipleBusinessConfigMap.get(leiType) != null && multipleBusinessConfigMap.get(leiType).equals("Yes");
        }
        return false;
    }

    private void validateAiAndOuEntities(Head head) {
        logger.info("Validating AI-OU Entities");
        AiEntity aiEntity = validateAiEntity(head.getAiId());
        validateAiEntityStatus(aiEntity);
        if (BusinessUtil.isNotNullOrEmpty(head.getOuId())) {
            OuEntity ouEntity = validateOuEntity(head.getOuId());
            validateOuEntityStatus(ouEntity);
            validateAiOuMapping(head.getAiId(), head.getOuId());
        }
    }

    private void validateAiOuMapping(String aiId, String ouId) {
        Optional<AiOuMapEntity> aiOuMapEntity = dbOperationService.retrieveAiOuMapEntity(aiId, ouId);
        if (aiOuMapEntity.isEmpty()) {
            generateValidationException("AI-OU mapping not exist for AI: " + aiId + " OU: " + ouId,
                    ValidationConstants.HEAD_AI_ID, ErrorScenarioCode.GEN_0005);
        }

        if (EntityStatus.INACTIVE == aiOuMapEntity.get().getStatus()) {
            generateValidationException("AI-OU mapping is not active for AI: " + aiId + " OU: " + ouId,
                    ValidationConstants.HEAD_AI_ID, ErrorScenarioCode.GEN_0006);
        }
    }

    private AiEntity validateAiEntity(String aiId) {
        Optional<AiEntity> optionalAiEntity = dbOperationService.retrieveAiEntity(aiId);
        if (optionalAiEntity.isEmpty()) {
            generateValidationException("AiEntity doesn't exist for : " + aiId, ValidationConstants.HEAD_AI_ID,
                    ErrorScenarioCode.GEN_0001);
        }
        return optionalAiEntity.get();
    }

    private void validateAiEntityStatus(AiEntity aiEntity) {
        if (BusinessUtil.isNotNull(aiEntity) && !EntityStatus.ACTIVE.equals(aiEntity.getStatus())) {
            generateValidationException("AiEntity Status not valid : " + aiEntity.getStatus(),
                    "aiId", ErrorScenarioCode.GEN_0002);
        }
    }

    private OuEntity validateOuEntity(String ouId) {
        Optional<OuEntity> ouEntity = dbOperationService.retrieveOuEntity(ouId);
        if (ouEntity.isEmpty()) {
            generateValidationException("OuEntity doesn't exist for : " + ouId, ValidationConstants.HEAD_OU_ID,
                    ErrorScenarioCode.GEN_0003);
        }
        return ouEntity.get();
    }

    private void validateOuEntityStatus(OuEntity ouEntity) {
        if (BusinessUtil.isNotNull(ouEntity) && !EntityStatus.ACTIVE.equals(ouEntity.getStatus())) {
            generateValidationException("OuEntity status noy valid : " + ouEntity.getStatus(),
                    ValidationConstants.HEAD_OU_ID, ErrorScenarioCode.GEN_0004);
        }
    }

    public void validateAiOuMappings(AiOuMappingDetails aiOuMappingDetails) {
        validateAiEntity(aiOuMappingDetails.getAiId());
        validateOuEntity(aiOuMappingDetails.getOuId());
    }

    private void generateValidationException(String msg, String fieldName, ErrorScenarioCode scenarioCode) {
        throw BusinessUtil.generateValidationException(msg, fieldName, scenarioCode);
    }

    public void checkB2BIdValidations(CheckB2BIdRequest request) {
        logger.info("Business Validations for CheckB2BID Request");
        validateAiAndOuEntities(request.getCommonData().getHead());
    }
}
