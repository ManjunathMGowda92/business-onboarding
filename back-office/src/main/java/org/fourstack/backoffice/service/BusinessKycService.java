package org.fourstack.backoffice.service;

import lombok.RequiredArgsConstructor;
import org.fourstack.backoffice.constants.ResponseConstants;
import org.fourstack.backoffice.entity.kyc.KycRequestAuditEntity;
import org.fourstack.backoffice.entity.kyc.KycRequestEntity;
import org.fourstack.backoffice.enums.ErrorScenarioCode;
import org.fourstack.backoffice.enums.KycRequestType;
import org.fourstack.backoffice.enums.OperationStatus;
import org.fourstack.backoffice.exception.InvalidInputException;
import org.fourstack.backoffice.mapper.EntityMapper;
import org.fourstack.backoffice.mapper.ResponseMapper;
import org.fourstack.backoffice.model.business.kyc.KycBusinessRequest;
import org.fourstack.backoffice.model.business.kyc.OuKycResponse;
import org.fourstack.backoffice.repository.kyc.KycRequestAuditRepository;
import org.fourstack.backoffice.repository.kyc.KycRequestRepository;
import org.fourstack.backoffice.util.BackOfficeUtil;
import org.fourstack.backoffice.util.KeyGenerationUtil;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor(onConstructor_ = @Lazy)
public class BusinessKycService {

  private final KycRequestRepository kycRequestRepository;
  private final KycRequestAuditRepository kycAuditRepository;
  private final EntityMapper mapper;
  private final ResponseMapper responseMapper;


  public Collection<OuKycResponse> processBusinessKyc(KycBusinessRequest request) {
    KycRequestType requestType = BackOfficeUtil.getKycRequestType(request.getRequestType());
    return switch (requestType) {
      case CREATE_BUSINESS -> processNewBusinessRequest(request);
      case EDIT_BUSINESS -> processEditBusinessRequest(request);
      case null -> throw new InvalidInputException(ErrorScenarioCode.BO_KYC_INVALID_REQ_001.getErrorMsg() + " - null",
              ErrorScenarioCode.BO_KYC_INVALID_REQ_001.getErrorCode());
      default ->
              throw new InvalidInputException(ErrorScenarioCode.BO_KYC_INVALID_REQ_001.getErrorMsg() + " - " + request.getRequestType(),
                      ErrorScenarioCode.BO_KYC_INVALID_REQ_001.getErrorCode());
    };
  }

  private Collection<OuKycResponse> processEditBusinessRequest(KycBusinessRequest request) {
    String objectId = request.getEditInstitute().getObjectId();
    Optional<KycRequestEntity> optionalEntity = kycRequestRepository.findById(objectId);
    KycRequestEntity entity = optionalEntity.isPresent() ? mapper.constructKycEntityForEditBusiness(request, optionalEntity.get())
            : mapper.constructKycEntityForEditBusiness(request, null);
    Map<String, Boolean> ouExistenceMap = request.getOuExistenceMap();
    Map<String, OuKycResponse> ouKycResponseMap = constructOuResponseMap(ouExistenceMap);
    entity.setKycOuMap(ouKycResponseMap);
    String entityKey = KeyGenerationUtil.generateKycRequestKey(entity.getObjectId(), KycRequestType.EDIT_BUSINESS.name());
    entity.setKey(entityKey);
    kycRequestRepository.save(entity);
    persistKycAuditEntity(entity, KycRequestType.EDIT_BUSINESS.name());
    return ouKycResponseMap.values();
  }

  private Collection<OuKycResponse> processNewBusinessRequest(KycBusinessRequest request) {
    KycRequestEntity entity = mapper.constructKycEntityForNewBusiness(request);
    Map<String, Boolean> ouExistenceMap = request.getOuExistenceMap();
    Map<String, OuKycResponse> ouKycResponseMap = constructOuResponseMap(ouExistenceMap);
    entity.setKycOuMap(ouKycResponseMap);
    String entityKey = KeyGenerationUtil.generateKycRequestKey(entity.getObjectId(), KycRequestType.CREATE_BUSINESS.name());
    entity.setKey(entityKey);
    kycRequestRepository.save(entity);
    persistKycAuditEntity(entity, KycRequestType.CREATE_BUSINESS.name());
    return ouKycResponseMap.values();
  }

  private Map<String, OuKycResponse> constructOuResponseMap(Map<String, Boolean> ouExistenceMap) {
    Map<String, OuKycResponse> ouKycResponseMap = new HashMap<>();
    if (BackOfficeUtil.isNotNull(ouExistenceMap)) {
      for (Map.Entry<String, Boolean> entry : ouExistenceMap.entrySet()) {
        Boolean isOuExist = entry.getValue();
        OuKycResponse ouKycResponse;
        String ouId = entry.getKey();
        if (Boolean.TRUE.equals(isOuExist)) {
          ouKycResponse = responseMapper.buildOuKycResponse(ouId, OperationStatus.SUCCESS,
                  ResponseConstants.KYC_SUCCESS, null, null);
        } else {
          ouKycResponse = responseMapper.buildOuKycResponse(ouId, OperationStatus.FAILURE, ErrorScenarioCode.BO_KYC_OU_FAIL_0001.getErrorMsg(),
                  ErrorScenarioCode.BO_KYC_OU_FAIL_0001.getErrorCode(), "kycRequestedOuIds");
        }
        ouKycResponseMap.put(ouId, ouKycResponse);
      }
    }
    return ouKycResponseMap;
  }

  private void persistKycAuditEntity(KycRequestEntity entity, String requestType) {
    KycRequestAuditEntity kycAuditEntity = mapper.constructKycAuditEntity(entity);
    String auditEntityKey = KeyGenerationUtil.generateKycAuditKey(entity.getObjectId(), requestType);
    kycAuditEntity.setKey(auditEntityKey);
    kycAuditRepository.save(kycAuditEntity);
  }
}
