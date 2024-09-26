package org.fourstack.business.service;

import lombok.RequiredArgsConstructor;
import org.fourstack.business.constants.AppConstants;
import org.fourstack.business.enums.OperationStatus;
import org.fourstack.business.exceptions.MissingFieldException;
import org.fourstack.business.exceptions.ValidationException;
import org.fourstack.business.mapper.ResponseMapper;
import org.fourstack.business.model.Acknowledgement;
import org.fourstack.business.model.ActivateB2BRequest;
import org.fourstack.business.model.B2BIdRegisterRequest;
import org.fourstack.business.model.BusinessRegisterRequest;
import org.fourstack.business.model.CheckBusinessRequest;
import org.fourstack.business.model.CommonData;
import org.fourstack.business.model.EditB2BIdRequest;
import org.fourstack.business.model.Head;
import org.fourstack.business.model.SearchBusinessRequest;
import org.fourstack.business.model.Transaction;
import org.fourstack.business.model.ValidationResult;
import org.fourstack.business.utils.BusinessUtil;
import org.fourstack.business.validator.FieldFormatValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor(onConstructor_ = @Lazy)
public class BusinessService {
    private static final Logger logger = LoggerFactory.getLogger(BusinessService.class);
    private final FieldFormatValidator formatValidator;
    private final KafkaPublisherService publisherService;
    private final ResponseMapper responseMapper;

    public ResponseEntity<Acknowledgement> processRequest(Object request, String endPoint) {
        try {
            Acknowledgement acknowledgement = switch (request) {
                case BusinessRegisterRequest businessRequest -> processBusinessRequest(businessRequest, endPoint);
                case B2BIdRegisterRequest businessRequest -> processCreateB2BRequest(businessRequest, endPoint);
                case CheckBusinessRequest businessRequest -> processCheckBusinessRequest(businessRequest, endPoint);
                case SearchBusinessRequest businessRequest -> processSearchBusinessRequest(businessRequest, endPoint);
                case EditB2BIdRequest businessRequest -> processEditB2BIdRequest(businessRequest, endPoint);
                case ActivateB2BRequest businessRequest -> processActivateB2BRequest(businessRequest, endPoint);
                default -> generateNegativeAck(endPoint, AppConstants.ERROR_500, AppConstants.INTERNAL_SERVER_ERROR,
                        "Unknown Request Type", null, null);
            };
            return generateResponse(acknowledgement);
        } catch (ValidationException exception) {
            logger.error("{} - Validation Exception occurred - {}", this.getClass().getSimpleName(),
                    exception.getErrorResponse());
            CommonData commonData = getCommonDataRequest(request);
            return generateResponse(generateNegativeAck(endPoint, exception.getErrorCode(), exception.getErrorMessage(),
                    exception.getFieldName(), commonData.getHead(), commonData.getTxn()));
        } catch (MissingFieldException exception) {
            logger.error("{} - Missing Filed Exception - {}", this.getClass().getSimpleName(),
                    exception.getFieldName());
            CommonData commonData = getCommonDataRequest(request);
            return generateResponse(generateNegativeAck(endPoint, "INP0001", "Mandatory field missing",
                    exception.getFieldName(), commonData.getHead(), commonData.getTxn()));
        } catch (Exception exception) {
            return generateResponse(generateNegativeAck(endPoint, AppConstants.ERROR_500, AppConstants.INTERNAL_SERVER_ERROR,
                    null, null, null));
        }
    }

    private CommonData getCommonDataRequest(Object request) {
        CommonData commonData = getCommonData(request);
        return BusinessUtil.isNotNull(commonData) ? commonData : new CommonData();
    }

    private static CommonData getCommonData(Object request) {
        return switch (request) {
            case BusinessRegisterRequest businessRequest -> businessRequest.getCommonData();
            case B2BIdRegisterRequest businessRequest -> businessRequest.getCommonData();
            case CheckBusinessRequest businessRequest -> businessRequest.getCommonData();
            case SearchBusinessRequest businessRequest -> businessRequest.getCommonData();
            default -> new CommonData();
        };
    }

    private Acknowledgement processActivateB2BRequest(ActivateB2BRequest request, String endPoint) {
        logger.info("Execution process started for Activate Deactivate B2B Request");
        return publisherService.publishBusiness(request, endPoint);
    }

    private Acknowledgement processEditB2BIdRequest(EditB2BIdRequest request, String endPoint) {
        logger.info("Execution process started for EditB2BIdRequest");
        return publisherService.publishBusiness(request, endPoint);
    }

    private Acknowledgement processBusinessRequest(BusinessRegisterRequest request, String endPoint) {
        logger.info("Execution process started for BusinessRegisterRequest");
        ValidationResult validationResult = formatValidator.validateBusiness(request);
        if (BusinessUtil.isNotNull(validationResult) && OperationStatus.SUCCESS.equals(validationResult.status())) {
            return publisherService.publishBusiness(request, endPoint);
        }
        return generateNegativeAck(endPoint, AppConstants.ERROR_500, AppConstants.INTERNAL_SERVER_ERROR,
                null, null, null);
    }

    private Acknowledgement processCreateB2BRequest(B2BIdRegisterRequest request, String endPoint) {
        logger.info("Execution process started for B2BIdRegisterRequest");
        ValidationResult result = formatValidator.validateB2BIdRequest(request);
        if (BusinessUtil.isNotNull(result) && OperationStatus.SUCCESS.equals(result.status())) {
            return publisherService.publishBusiness(request, endPoint);
        }
        return generateNegativeAck(endPoint, AppConstants.ERROR_500, AppConstants.INTERNAL_SERVER_ERROR,
                null, null, null);
    }

    private Acknowledgement processCheckBusinessRequest(CheckBusinessRequest request, String endPoint) {
        logger.info("Execution process started for CheckBusinessRequest");
        ValidationResult result = formatValidator.validateCheckBusiness(request);
        if (BusinessUtil.isNotNull(result) && OperationStatus.SUCCESS.equals(result.status())) {
            return publisherService.publishBusiness(request, endPoint);
        }
        return generateNegativeAck(endPoint, AppConstants.ERROR_500, AppConstants.INTERNAL_SERVER_ERROR,
                null, null, null);
    }

    private Acknowledgement processSearchBusinessRequest(SearchBusinessRequest request, String endPoint) {
        logger.info("Execution process started for SearchBusinessRequest");
        ValidationResult result = formatValidator.validateSearchBusiness(request);
        if (BusinessUtil.isNotNull(result) && OperationStatus.SUCCESS.equals(result.status())) {
            return publisherService.publishBusiness(request, endPoint);
        }
        return generateNegativeAck(endPoint, AppConstants.ERROR_500, AppConstants.INTERNAL_SERVER_ERROR,
                null, null, null);
    }

    private Acknowledgement generateNegativeAck(String endPoint, String errorCode, String message,
                                                String fieldName, Head head, Transaction txn) {
        String msgId = Objects.nonNull(head) ? head.getMsgId()
                : BusinessUtil.generateUniqueId(AppConstants.PREFIX_MSG);
        String txnId = Objects.nonNull(txn) ? txn.getId()
                : BusinessUtil.generateUniqueId(AppConstants.PREFIX_TXN);
        return responseMapper.getFailureAck(msgId, txnId, endPoint, errorCode, message, fieldName);
    }

    private ResponseEntity<Acknowledgement> generateResponse(Acknowledgement acknowledgement) {
        OperationStatus result = acknowledgement.getResult();
        if (OperationStatus.SUCCESS.equals(result)) {
            return ResponseEntity.ok(acknowledgement);
        } else {
            String errorCode = acknowledgement.getErrorCode();
            if (AppConstants.ERROR_500.equals(errorCode)) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(acknowledgement);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(acknowledgement);
            }
        }
    }
}
