package org.fourstack.business.mapper;

import lombok.RequiredArgsConstructor;
import org.fourstack.business.entity.AiEntity;
import org.fourstack.business.entity.AiOuMapEntity;
import org.fourstack.business.entity.BusinessEntity;
import org.fourstack.business.entity.MainOrgIdEntity;
import org.fourstack.business.entity.OuEntity;
import org.fourstack.business.enums.BooleanStatus;
import org.fourstack.business.enums.OperationStatus;
import org.fourstack.business.model.B2BAvailabilityResponse;
import org.fourstack.business.model.B2BIdRegisterRequest;
import org.fourstack.business.model.B2BIdRegisterResponse;
import org.fourstack.business.model.BusinessDetails;
import org.fourstack.business.model.BusinessRegisterRequest;
import org.fourstack.business.model.BusinessRegisterResponse;
import org.fourstack.business.model.CheckB2BIdRequest;
import org.fourstack.business.model.CheckB2BIdResponse;
import org.fourstack.business.model.CheckBusinessRequest;
import org.fourstack.business.model.CheckBusinessResponse;
import org.fourstack.business.model.CheckInstitute;
import org.fourstack.business.model.CheckInstituteResponse;
import org.fourstack.business.model.CommonRequestData;
import org.fourstack.business.model.CommonResponseData;
import org.fourstack.business.model.EntityInfo;
import org.fourstack.business.model.Head;
import org.fourstack.business.model.Institute;
import org.fourstack.business.model.InstituteInfo;
import org.fourstack.business.model.Response;
import org.fourstack.business.model.SearchBusinessRequest;
import org.fourstack.business.model.SearchBusinessResponse;
import org.fourstack.business.model.SearchResponse;
import org.fourstack.business.model.backoffice.AiBackOfficeResponse;
import org.fourstack.business.model.backoffice.AiDetails;
import org.fourstack.business.model.backoffice.AiOuBackOfficeResponse;
import org.fourstack.business.model.backoffice.AiOuMappingDetails;
import org.fourstack.business.model.backoffice.OuBackOfficeResponse;
import org.fourstack.business.model.backoffice.OuDetails;
import org.fourstack.business.service.DbOperationService;
import org.fourstack.business.utils.BusinessUtil;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Component
@RequiredArgsConstructor(onConstructor_ = @Lazy)
public class ResponseMapper {
    private final DbOperationService dbOperationService;

    public BusinessRegisterResponse generateSuccessBusinessResponse(BusinessRegisterRequest request) {
        BusinessRegisterResponse businessResponse = generateBusinessRegisterResponse(request);
        CommonResponseData commonData = businessResponse.getCommonData();
        Head head = commonData.getHead();
        Response response = generateSuccessResponse(head.getMsgId());
        head.setMsgId(BusinessUtil.generateAlphaNumericID(32, "MSG"));
        commonData.setResponse(response);
        businessResponse.setCommonData(commonData);
        return businessResponse;
    }

    public BusinessRegisterResponse generateFailureBusinessResponse(BusinessRegisterRequest request, String errorCode,
                                                                    String errorMsg, String errorField) {
        BusinessRegisterResponse businessResponse = generateBusinessRegisterResponse(request);
        CommonResponseData commonData = businessResponse.getCommonData();
        Head head = commonData.getHead();
        Response response = generateFailureResponse(head.getMsgId(), errorCode, errorMsg, errorField);
        head.setMsgId(BusinessUtil.generateAlphaNumericID(32, "MSG"));
        commonData.setResponse(response);
        businessResponse.setCommonData(commonData);
        return businessResponse;
    }

    public B2BIdRegisterResponse generateSuccessB2BResponse(B2BIdRegisterRequest request) {
        B2BIdRegisterResponse businessResponse = generateB2BRegisterResponse(request);
        CommonResponseData commonData = businessResponse.getCommonData();
        Head head = commonData.getHead();
        Response response = generateSuccessResponse(head.getMsgId());
        head.setMsgId(BusinessUtil.generateAlphaNumericID(32, "MSG"));
        commonData.setResponse(response);
        businessResponse.setCommonData(commonData);
        return businessResponse;
    }

    public B2BIdRegisterResponse generateFailureB2BResponse(B2BIdRegisterRequest request, String errorCode,
                                                            String errorMsg, String errorField) {
        B2BIdRegisterResponse businessResponse = generateB2BRegisterResponse(request);
        CommonResponseData commonData = businessResponse.getCommonData();
        Head head = commonData.getHead();
        Response response = generateFailureResponse(head.getMsgId(), errorCode, errorMsg, errorField);
        head.setMsgId(BusinessUtil.generateAlphaNumericID(32, "MSG"));
        commonData.setResponse(response);
        return businessResponse;
    }

    private B2BIdRegisterResponse generateB2BRegisterResponse(B2BIdRegisterRequest request) {
        B2BIdRegisterResponse response = new B2BIdRegisterResponse();
        CommonResponseData responseData = constructCommonDataForResponse(request.getCommonData());
        response.setCommonData(responseData);
        response.setAdditionalInfoList(request.getAdditionalInfoList());
        response.setRegB2BIds(request.getRegB2BIds());
        response.setOnboardingB2BIds(request.getOnboardingB2BIds());
        return response;
    }

    private Response generateSuccessResponse(String msgId) {
        return Response.builder().requestMsgId(msgId)
                .result(OperationStatus.SUCCESS)
                .build();
    }

    private Response generateFailureResponse(String msgId, String errorCode, String errMsg, String errField) {
        return Response.builder().requestMsgId(msgId)
                .result(OperationStatus.FAILURE)
                .errorCode(errorCode)
                .errorMsg(errMsg)
                .errorField(errField)
                .build();
    }

    public BusinessRegisterResponse generateBusinessRegisterResponse(BusinessRegisterRequest request) {
        BusinessRegisterResponse response = new BusinessRegisterResponse();
        CommonResponseData responseData = constructCommonDataForResponse(request.getCommonData());
        response.setCommonData(responseData);
        response.setInstitute(request.getInstitute());
        response.setAdditionalInfoList(request.getAdditionalInfoList());
        return response;
    }

    private CommonResponseData constructCommonDataForResponse(CommonRequestData commonData) {
        CommonResponseData responseData = new CommonResponseData();
        responseData.setHead(commonData.getHead());
        responseData.setTxn(commonData.getTxn());
        return responseData;
    }

    public CheckInstituteResponse generateCheckInstituteResponse(CheckInstitute checkInstitute, boolean isBusinessExist,
                                                                 boolean isMultipleBusinessAllowed) {
        CheckInstituteResponse instituteResponse = new CheckInstituteResponse();
        InstituteInfo instituteInfo = new InstituteInfo();
        instituteInfo.setIsMultipleOrgAllowed(isMultipleBusinessAllowed ? BooleanStatus.YES : BooleanStatus.NO);
        if (isBusinessExist) {
            instituteInfo.setIsBusinessExist(BooleanStatus.YES);
            Map<String, BusinessEntity> businessEntityMap = dbOperationService.retrieveBusinessEntities(checkInstitute.getValue());
            List<BusinessDetails> businessDetails = businessEntityMap.values()
                    .stream().map(BusinessEntity::getInstitute)
                    .filter(Objects::nonNull)
                    .map(this::extractBusinessDetails)
                    .toList();
            instituteInfo.setBusinessDetails(businessDetails);
        } else {
            instituteInfo.setIsBusinessExist(BooleanStatus.NO);
            BusinessDetails businessDetails = getBusinessDetails(checkInstitute.getRegisteredName(), Collections.emptyList());
            instituteInfo.setBusinessDetails(List.of(businessDetails));
        }
        return instituteResponse;
    }

    public CheckBusinessResponse generateSuccessCheckBusinessResponse(CheckBusinessRequest request,
                                                                      CheckInstituteResponse instituteResponse) {
        CheckBusinessResponse businessResponse = generateCheckBusinessResponse(request);
        businessResponse.setInstituteStatus(instituteResponse);
        CommonResponseData commonData = businessResponse.getCommonData();
        Head head = commonData.getHead();
        Response response = generateSuccessResponse(head.getMsgId());
        head.setMsgId(BusinessUtil.generateAlphaNumericID(32, "MSG"));
        commonData.setResponse(response);
        return businessResponse;
    }

    public CheckBusinessResponse generateFailureCheckBusinessResponse(CheckBusinessRequest request, String errorCode,
                                                                      String errorMsg, String errorField) {
        CheckBusinessResponse businessResponse = generateCheckBusinessResponse(request);
        CommonResponseData commonData = businessResponse.getCommonData();
        Head head = commonData.getHead();
        Response response = generateFailureResponse(head.getMsgId(), errorCode, errorMsg, errorField);
        head.setMsgId(BusinessUtil.generateAlphaNumericID(32, "MSG"));
        commonData.setResponse(response);
        return businessResponse;
    }

    private CheckBusinessResponse generateCheckBusinessResponse(CheckBusinessRequest request) {
        CheckBusinessResponse response = new CheckBusinessResponse();
        response.setCheckInstitute(request.getCheckInstitute());
        response.setAdditionalInfoList(request.getAdditionalInfoList());
        response.setCommonData(constructCommonDataForResponse(request.getCommonData()));
        return response;
    }

    public SearchBusinessResponse generateFailureSearchBusinessResponse(SearchBusinessRequest request, String errorCode,
                                                                        String errorMsg, String errorField) {
        SearchBusinessResponse businessResponse = generateSearchBusinessResponse(request, Collections.emptyList());
        CommonResponseData commonData = businessResponse.getCommonData();
        Head head = commonData.getHead();
        Response response = generateFailureResponse(head.getMsgId(), errorCode, errorMsg, errorField);
        head.setMsgId(BusinessUtil.generateAlphaNumericID(32, "MSG"));
        commonData.setResponse(response);
        return businessResponse;
    }

    public SearchBusinessResponse generateSuccessSearchResponse(SearchBusinessRequest request, List<EntityInfo> entityInfoList) {
        SearchBusinessResponse businessResponse = generateSearchBusinessResponse(request, entityInfoList);
        CommonResponseData commonData = businessResponse.getCommonData();
        Head head = commonData.getHead();
        Response response = generateSuccessResponse(head.getMsgId());
        head.setMsgId(BusinessUtil.generateAlphaNumericID(32, "MSG"));
        commonData.setResponse(response);
        return businessResponse;
    }

    private SearchBusinessResponse generateSearchBusinessResponse(SearchBusinessRequest request,
                                                                  List<EntityInfo> entityInfoList) {
        SearchBusinessResponse response = new SearchBusinessResponse();
        response.setCommonData(constructCommonDataForResponse(request.getCommonData()));
        response.setOnboardingB2BIds(request.getOnboardingB2BIds());
        response.setSearch(request.getSearch());
        response.setAdditionalInfoList(request.getAdditionalInfoList());
        SearchResponse searchResponse = new SearchResponse();
        searchResponse.setEntityInfoList(entityInfoList);
        response.setSearchResult(searchResponse);
        return response;
    }

    public CheckB2BIdResponse generateFailureCheckB2BResponse(CheckB2BIdRequest request,
                                                              String errorCode, String errorMsg, String errorField) {
        CheckB2BIdResponse businessResponse = getCheckB2BIdResponse(request);
        CommonResponseData commonData = businessResponse.getCommonData();
        Head head = commonData.getHead();
        Response response = generateFailureResponse(head.getMsgId(), errorCode, errorMsg, errorField);
        head.setMsgId(BusinessUtil.generateAlphaNumericID(32, "MSG"));
        commonData.setResponse(response);
        return businessResponse;
    }

    public CheckB2BIdResponse generateSuccessCheckB2BResponse(CheckB2BIdRequest request,
                                                              List<B2BAvailabilityResponse> b2bIdStatuses) {
        CheckB2BIdResponse businessResponse = getCheckB2BIdResponse(request);
        businessResponse.setB2bIdStatuses(b2bIdStatuses);
        CommonResponseData commonData = businessResponse.getCommonData();
        Head head = commonData.getHead();
        Response response = generateSuccessResponse(head.getMsgId());
        head.setMsgId(BusinessUtil.generateAlphaNumericID(32, "MSG"));
        commonData.setResponse(response);
        return businessResponse;
    }

    private CheckB2BIdResponse getCheckB2BIdResponse(CheckB2BIdRequest request) {
        CheckB2BIdResponse businessResponse = new CheckB2BIdResponse();
        businessResponse.setCommonData(constructCommonDataForResponse(request.getCommonData()));
        businessResponse.setCheckB2BIds(request.getCheckB2BIds());
        businessResponse.setAdditionalInfoList(request.getAdditionalInfoList());
        return businessResponse;
    }

    private BusinessDetails extractBusinessDetails(Institute institute) {
        Optional<MainOrgIdEntity> orgIdEntity = dbOperationService.retrieveOrgIdEntity(institute.getObjectId());
        if (orgIdEntity.isPresent()) {
            Set<String> b2BIds = BusinessUtil.extractAllB2BIds(orgIdEntity.get());
            return getBusinessDetails(orgIdEntity.get().getBusinessName(), b2BIds);
        } else {
            return getBusinessDetails(institute.getName(), List.of(institute.getDefaultB2bId()));
        }
    }

    private BusinessDetails getBusinessDetails(String businessName, Collection<String> b2bIds) {
        BusinessDetails details = new BusinessDetails();
        details.setBusinessName(businessName);
        details.setB2bIds(b2bIds);
        return details;
    }

    public AiBackOfficeResponse constructSuccessAiResponse(AiEntity entity) {
        AiBackOfficeResponse response = new AiBackOfficeResponse();
        response.setAiId(entity.getId());
        response.setName(entity.getName());
        response.setSubscriberId(entity.getSubscriberId());
        response.setStatus(entity.getStatus().name());
        response.setType(entity.getAiType().name());
        response.setCreatedTimeStamp(entity.getCreatedTimeStamp());
        response.setLastModifiedTimeStamp(entity.getLastModifiedTimeStamp());
        response.setResponse(generateSuccessResponse(null));
        return response;
    }

    public AiBackOfficeResponse constructFailureAiResponse(AiDetails aiDetails, String errorCode,
                                                           String errorMsg, String fieldName) {
        AiBackOfficeResponse response = new AiBackOfficeResponse();
        response.setAiId(aiDetails.getAiId());
        response.setName(aiDetails.getName());
        response.setSubscriberId(aiDetails.getSubscriberId());
        response.setStatus(aiDetails.getStatus());
        response.setType(aiDetails.getType());
        response.setResponse(generateFailureResponse(null, errorCode, errorMsg, fieldName));
        return response;
    }

    public OuBackOfficeResponse constructSuccessOuResponse(OuEntity entity) {
        OuBackOfficeResponse response = new OuBackOfficeResponse();
        response.setOuId(entity.getId());
        response.setName(entity.getName());
        response.setStatus(entity.getStatus().name());
        response.setCreatedTimeStamp(entity.getCreatedTimeStamp());
        response.setLastModifiedTimeStamp(entity.getLastModifiedTimeStamp());
        response.setResponse(generateSuccessResponse(null));
        return response;
    }

    public OuBackOfficeResponse constructFailureOuResponse(OuDetails ouDetails, String errorCode,
                                                           String errorMsg, String fieldName) {
        OuBackOfficeResponse response = new OuBackOfficeResponse();
        response.setOuId(ouDetails.getOuId());
        response.setName(ouDetails.getName());
        response.setStatus(ouDetails.getStatus());
        response.setResponse(generateFailureResponse(null, errorCode, errorMsg, fieldName));
        return response;
    }

    public AiOuBackOfficeResponse constructSuccessAiOuResponse(AiOuMapEntity entity) {
        AiOuBackOfficeResponse response = new AiOuBackOfficeResponse();
        response.setAiId(entity.getAiId());
        response.setOuId(entity.getOuId());
        response.setWebhookUrl(entity.getWebhookUrl());
        response.setEncryptionDetails(entity.getEncryptionDetails());
        response.setStatus(entity.getStatus().name());
        response.setCreatedTimeStamp(entity.getCreatedTimeStamp());
        response.setLastModifiedTimeStamp(entity.getLastModifiedTimeStamp());
        return response;
    }

    public AiOuBackOfficeResponse constructFailureAiOuResponse(AiOuMappingDetails details, String errorCode,
                                                               String errorMsg, String fieldName) {
        AiOuBackOfficeResponse response = new AiOuBackOfficeResponse();
        response.setAiId(details.getAiId());
        response.setOuId(details.getOuId());
        response.setWebhookUrl(details.getWebhookUrl());
        response.setEncryptionDetails(details.getEncryptionDetails());
        response.setStatus(details.getStatus());
        response.setResponse(generateFailureResponse(null, errorCode, errorMsg, fieldName));
        return response;
    }

}
