package org.fourstack.backoffice.mapper;

import org.fourstack.backoffice.entity.AgentInstitutionEntity;
import org.fourstack.backoffice.entity.AiOuMappingEntity;
import org.fourstack.backoffice.entity.OperationUnitEntity;
import org.fourstack.backoffice.enums.ErrorScenarioCode;
import org.fourstack.backoffice.enums.OperationStatus;
import org.fourstack.backoffice.model.AiOuMappingResponse;
import org.fourstack.backoffice.model.AiResponse;
import org.fourstack.backoffice.model.BackOfficeAck;
import org.fourstack.backoffice.model.BackOfficeListResponse;
import org.fourstack.backoffice.model.BackOfficeResponse;
import org.fourstack.backoffice.model.ErrorResponse;
import org.fourstack.backoffice.model.OuResponse;
import org.fourstack.backoffice.model.business.kyc.KycBusinessRequest;
import org.fourstack.backoffice.model.business.kyc.KycBusinessResponse;
import org.fourstack.backoffice.model.business.kyc.OuKycResponse;
import org.fourstack.backoffice.util.BackOfficeUtil;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
public class ResponseMapper {

  public AiResponse mapAiEntityToResponse(AgentInstitutionEntity aiEntity) {
    AiResponse response = new AiResponse();
    response.setAgentInstitutionId(aiEntity.getId());
    response.setAgentInstitutionName(aiEntity.getName());
    response.setAgentInstitutionAliasName(aiEntity.getAlias());
    response.setDescription(aiEntity.getDescription());
    response.setSubscriberId(aiEntity.getSubscriberId());
    response.setType(aiEntity.getType().getType());
    response.setRegisteredAddress(aiEntity.getRegisteredAddress());
    response.setCommunicationAddress(aiEntity.getCommunicationAddress());
    response.setStatus(aiEntity.getStatus());
    response.setCreatedTimeStamp(aiEntity.getCreatedTimeStamp());
    response.setLastModifiedTimeStamp(aiEntity.getLastModifiedTimeStamp());
    response.setLinkedOus(aiEntity.getLinkedOus());
    return response;
  }

  public BackOfficeResponse constructResponse(Object request) {
    BackOfficeResponse response = new BackOfficeResponse();
    response.setResponse(request);
    response.setAck(constructSuccessAck());
    return response;
  }

  public BackOfficeListResponse constructListResponse(List<?> request) {
    BackOfficeListResponse response = new BackOfficeListResponse();
    response.setResponseList(request);
    response.setAck(constructSuccessAck());
    return response;
  }

  public BackOfficeResponse constructFailureResponse(ErrorScenarioCode scenarioCode, String fieldName) {
    return constructFailureResponse(scenarioCode.getErrorCode(), scenarioCode.getErrorMsg(), fieldName);
  }

  public BackOfficeResponse constructFailureResponse(String errorCode, String errorMsg, String errorField) {
    BackOfficeResponse response = new BackOfficeResponse();
    response.setAck(constructAck(OperationStatus.FAILURE, errorCode, errorMsg, errorField));
    return response;
  }

  public BackOfficeListResponse constructFailureListResponse(ErrorScenarioCode scenarioCode, String fieldName) {
    return constructFailureListResponse(scenarioCode.getErrorCode(), scenarioCode.getErrorMsg(), fieldName);
  }

  public BackOfficeListResponse constructFailureListResponse(String errorCode, String errorMsg, String errorField) {
    BackOfficeListResponse response = new BackOfficeListResponse();
    response.setAck(constructAck(OperationStatus.FAILURE, errorCode, errorMsg, errorField));
    return response;
  }

  public BackOfficeAck constructSuccessAck() {
    return constructAck(OperationStatus.SUCCESS, null, null, null);
  }

  public BackOfficeAck constructAck(OperationStatus status, String errorCode, String errorMsg, String errorField) {
    BackOfficeAck ack = new BackOfficeAck();
    ack.setStatus(status);
    ack.setErrorCode(errorCode);
    ack.setErrorMsg(errorMsg);
    ack.setErrorField(errorField);
    return ack;
  }

  public OuResponse mapOuEntityToResponse(OperationUnitEntity ouEntity) {
    OuResponse response = new OuResponse();
    response.setOuId(ouEntity.getId());
    response.setOperationUnitName(ouEntity.getName());
    response.setOperationUnitAliasName(ouEntity.getAlias());
    response.setDescription(ouEntity.getDescription());
    response.setMailId(ouEntity.getMailId());
    response.setRegisteredAddress(ouEntity.getRegisteredAddress());
    response.setCommunicationAddress(ouEntity.getCommunicationAddress());
    response.setBankDetails(ouEntity.getBankDetails());
    response.setStatus(ouEntity.getStatus());
    response.setCreatedTimeStamp(ouEntity.getCreatedTimeStamp());
    response.setLastModifiedTimeStamp(ouEntity.getLastModifiedTimeStamp());
    return response;
  }

  public AiOuMappingResponse mapToAiOuResponse(AiOuMappingEntity entity) {
    AiOuMappingResponse response = new AiOuMappingResponse();
    response.setAiId(entity.getAiId());
    response.setAiName(entity.getAiName());
    response.setOuId(entity.getOuId());
    response.setOuName(entity.getOuName());
    response.setDescription(entity.getDescription());
    response.setWebhookUrl(entity.getWebhookUrl());
    response.setEncryptionDetails(entity.getEncryptionDetails());
    response.setStatus(entity.getStatus());
    response.setCreatedTimeStamp(entity.getCreatedTimeStamp());
    response.setLastModifiedTimeStamp(entity.getLastModifiedTimeStamp());
    return response;
  }

  public KycBusinessResponse mapToKycResponse(KycBusinessRequest request, List<OuKycResponse> kycOuResponseList) {
    String objectId = getObjectId(request);
    String txnId = getTxnId(request);
    return buildKycResponse(objectId, txnId, kycOuResponseList);
  }

  public KycBusinessResponse buildKycResponse(String objectId, String txnId, List<OuKycResponse> kycOuResponseList) {
    KycBusinessResponse response = new KycBusinessResponse();
    response.setTimeStamp(BackOfficeUtil.getCurrentTimeStamp());
    response.setObjectId(objectId);
    response.setTxnId(txnId);
    response.setKycOuResponse(kycOuResponseList);
    response.setKycStatus(getKycStatus(kycOuResponseList));
    return response;
  }

  private OperationStatus getKycStatus(List<OuKycResponse> kycOuResponseList) {
    if (BackOfficeUtil.isCollectionNotNullOrEmpty(kycOuResponseList)) {
      Optional<OperationStatus> failureResponse = kycOuResponseList.stream()
              .map(OuKycResponse::getResult)
              .filter(Objects::nonNull)
              .filter(OperationStatus.FAILURE::equals)
              .findAny();
      Optional<OperationStatus> successResponse = kycOuResponseList.stream()
              .map(OuKycResponse::getResult)
              .filter(Objects::nonNull)
              .filter(OperationStatus.SUCCESS::equals)
              .findAny();
      if (failureResponse.isPresent() && successResponse.isPresent()) {
        return OperationStatus.PARTIAL;
      }
      if (failureResponse.isPresent()) {
        return OperationStatus.FAILURE;
      }
      if (successResponse.isPresent()) {
        return OperationStatus.SUCCESS;
      }

    }
    return OperationStatus.FAILURE;
  }

  private String getTxnId(KycBusinessRequest request) {
    if (BackOfficeUtil.isNotNull(request.getTxn())) {
      return request.getTxn().getId();
    }
    return null;
  }

  private String getObjectId(KycBusinessRequest request) {
    if (BackOfficeUtil.isNotNull(request.getInstitute())) {
      return request.getInstitute().getObjectId();
    } else if (BackOfficeUtil.isNotNull(request.getEditInstitute())) {
      return request.getEditInstitute().getObjectId();
    } else {
      return null;
    }
  }


  public OuKycResponse buildOuKycResponse(String ouId, OperationStatus status, String reason,
                                          String errorCode, String errorField) {
    return OuKycResponse.builder()
            .ouId(ouId)
            .result(status)
            .reason(reason)
            .errorCode(errorCode)
            .errorField(errorField)
            .build();
  }

  public KycBusinessResponse mapKycErrorResponse(String message, String errorCode,
                                                 String errorMessage, Exception exception) {
    ErrorResponse errorResponse = ErrorResponse.builder()
            .requestMsg(message)
            .errorCode(errorCode)
            .errorMsg(errorMessage)
            .errorDetail(exception.getLocalizedMessage())
            .build();
    KycBusinessResponse response = new KycBusinessResponse();
    response.setTimeStamp(BackOfficeUtil.getCurrentTimeStamp());
    response.setErrorResponse(errorResponse);
    return response;
  }

  public KycBusinessResponse mapKycErrorResponse(KycBusinessRequest request, List<OuKycResponse> kycOuResponseList,
                                                 String message, String errorCode, String errorMessage, Exception exception) {
    ErrorResponse errorResponse = ErrorResponse.builder()
            .requestMsg(message)
            .errorCode(errorCode)
            .errorMsg(errorMessage)
            .errorDetail(exception.getLocalizedMessage())
            .build();
    String objectId = getObjectId(request);
    String txnId = getTxnId(request);
    KycBusinessResponse response = buildKycResponse(objectId, txnId, kycOuResponseList);
    response.setTimeStamp(BackOfficeUtil.getCurrentTimeStamp());
    response.setErrorResponse(errorResponse);
    return response;
  }

}
