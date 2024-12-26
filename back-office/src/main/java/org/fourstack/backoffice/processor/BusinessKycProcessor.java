package org.fourstack.backoffice.processor;

import lombok.RequiredArgsConstructor;
import org.fourstack.backoffice.enums.ErrorScenarioCode;
import org.fourstack.backoffice.enums.OperationStatus;
import org.fourstack.backoffice.exception.InvalidInputException;
import org.fourstack.backoffice.exception.ValidationException;
import org.fourstack.backoffice.mapper.ResponseMapper;
import org.fourstack.backoffice.model.business.kyc.KycBusinessRequest;
import org.fourstack.backoffice.model.business.kyc.KycBusinessResponse;
import org.fourstack.backoffice.model.business.kyc.OuKycResponse;
import org.fourstack.backoffice.service.BusinessKycService;
import org.fourstack.backoffice.service.KafkaPublisherService;
import org.fourstack.backoffice.service.KafkaResponseAuditService;
import org.fourstack.backoffice.util.BackOfficeUtil;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor(onConstructor_ = @Lazy)
public class BusinessKycProcessor {
  private final BusinessKycService kycService;
  private final ResponseMapper mapper;
  private final KafkaResponseAuditService kycAuditService;
  private final KafkaPublisherService publisherService;


  public void processKyc(String message) {
    KycBusinessRequest kycBusinessRequest = getKycBusinessRequest(message);
    if (BackOfficeUtil.isNotNull(kycBusinessRequest)) {
      try {
        validate(kycBusinessRequest);
        Collection<OuKycResponse> ouKycResponses = kycService.processBusinessKyc(kycBusinessRequest);
        KycBusinessResponse response = mapper.mapToKycResponse(kycBusinessRequest, new ArrayList<>(ouKycResponses));
        kycAuditService.saveKafkaAudits(response, kycBusinessRequest.getRequestType());
        publisherService.publishKycResponse(response, response.getObjectId() != null ? response.getObjectId() : "");
      } catch (InvalidInputException e) {
        constructAndPublishErrorResponse(message, e, kycBusinessRequest, e.getErrorMessage(), e.getErrorCode(), null);
      } catch (ValidationException e) {
        constructAndPublishErrorResponse(message, e, kycBusinessRequest, e.getErrorMessage(), e.getErrorCode(), e.getErrorField());
      } catch (Exception e) {
        constructAndPublishErrorResponse(message, e, kycBusinessRequest, "", "", null);
      }
    }
  }

  private KycBusinessRequest getKycBusinessRequest(String message) {
    KycBusinessRequest kycBusinessRequest = null;
    try {
      kycBusinessRequest = transform(message);
    } catch (InvalidInputException e) {
      KycBusinessResponse response = mapper.mapKycErrorResponse(message, e.getErrorCode(), e.getErrorMessage(), e);
      publisherService.publishKycResponse(response, "");
    }
    return kycBusinessRequest;
  }

  private void constructAndPublishErrorResponse(String message, Exception e, KycBusinessRequest kycBusinessRequest,
                                                String errorMessage, String errorCode, String errorField) {
    Set<String> kycRequestedOuIds = kycBusinessRequest.getKycRequestedOuIds();
    if (BackOfficeUtil.isCollectionNotNullOrEmpty(kycRequestedOuIds)) {
      List<OuKycResponse> ouKycResponses = kycRequestedOuIds.stream()
              .map(ouId -> mapper.buildOuKycResponse(ouId, OperationStatus.FAILURE, errorMessage, errorCode, errorField))
              .toList();
      KycBusinessResponse response = mapper.mapKycErrorResponse(kycBusinessRequest, ouKycResponses, message,
              errorCode, errorMessage, e);
      publisherService.publishKycResponse(response, response.getObjectId() != null ? response.getObjectId() : "");
    }
  }

  private KycBusinessRequest transform(String message) {
    try {
      return BackOfficeUtil.convertToObject(message, KycBusinessRequest.class);
    } catch (Exception e) {
      kycAuditService.saveKafkaFailureAudits(message, "KYC-RESPONSE", "INBOUND", e);
      throw new InvalidInputException(e, ErrorScenarioCode.BO_KYC_INP_FAIL_001.getErrorMsg(),
              ErrorScenarioCode.BO_KYC_INP_FAIL_001.getErrorCode());
    }
  }

  private void validate(KycBusinessRequest request) {
  }
}
