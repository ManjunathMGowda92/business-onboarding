package org.fourstack.business.processor.inbound;

import org.fourstack.business.config.ApplicationConfig;
import org.fourstack.business.constants.BusinessConstants;
import org.fourstack.business.dao.service.TransactionDataService;
import org.fourstack.business.entity.event.CheckB2BIdEvent;
import org.fourstack.business.entity.event.Message;
import org.fourstack.business.enums.ErrorScenarioCode;
import org.fourstack.business.enums.EventType;
import org.fourstack.business.enums.TransactionType;
import org.fourstack.business.exception.InvalidInputException;
import org.fourstack.business.exception.ValidationException;
import org.fourstack.business.mapper.EntityMapper;
import org.fourstack.business.mapper.ResponseMapper;
import org.fourstack.business.model.Acknowledgement;
import org.fourstack.business.model.CheckB2BIdRequest;
import org.fourstack.business.model.CheckB2BIdResponse;
import org.fourstack.business.model.CommonRequestData;
import org.fourstack.business.model.MessageTransaction;
import org.fourstack.business.model.Transaction;
import org.fourstack.business.model.TransactionError;
import org.fourstack.business.validator.BusinessValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class CheckB2BIdInboundProcessor extends DefaultTransactionInboundProcessor {
    private static final Logger logger = LoggerFactory.getLogger(CheckB2BIdInboundProcessor.class);
    private final EntityMapper entityMapper;
    private final BusinessValidator businessValidator;
    private final ResponseMapper responseMapper;

    protected CheckB2BIdInboundProcessor(TransactionDataService transactionDataService,
                                         ApplicationConfig applicationConfig, EntityMapper entityMapper,
                                         BusinessValidator businessValidator, ResponseMapper responseMapper) {
        super(transactionDataService, applicationConfig);
        this.entityMapper = entityMapper;
        this.businessValidator = businessValidator;
        this.responseMapper = responseMapper;
    }

    @Override
    public MessageTransaction transform(Message<?, ?> message) {
        if (message.getRequest() instanceof CheckB2BIdRequest request) {
            logger.info("Transforming the Check B2B Id Request message");
            return constructTransaction(message, request);
        }
        logger.error("Invalid request received for processing");
        throw new InvalidInputException("Invalid message received", ErrorScenarioCode.GEN_0007.getErrorCode(),
                ErrorScenarioCode.GEN_0007.getErrorMsg());
    }

    private MessageTransaction constructTransaction(Message<?, ?> message, CheckB2BIdRequest request) {
        CheckB2BIdEvent event = new CheckB2BIdEvent();
        event.setRequest(request);
        CommonRequestData commonData = request.getCommonData();
        Transaction txn = commonData.getTxn();
        MessageTransaction transaction = entityMapper.constructTransaction(txn.getId(), commonData.getHead().getMsgId(),
                getEventType(), TransactionType.INQUIRY, txn.getTs(), event);
        if (message.getAck() instanceof Acknowledgement acknowledgement) {
            transaction.setAcknowledgement(acknowledgement);
        }
        return transaction;
    }

    /**
     * Method to validate business for CheckB2BId Request.
     *
     * @param transaction {@link MessageTransaction} object.
     * @throws ValidationException throws {@link ValidationException} if any failure.
     */
    @Override
    public void validate(MessageTransaction transaction) {
        if (transaction.getRequest() instanceof CheckB2BIdEvent event) {
            logger.info("Validating the CheckB2BId Business Request");
            try {
                businessValidator.checkB2BIdValidations(event.getRequest());
            } catch (ValidationException exception) {
                generateFailureResponse(transaction, event, exception.getErrorCode(), exception.getErrorMsg(),
                        exception.getErrorField(), exception.getMessage());
                transaction.setResponseStatus(HttpStatus.BAD_REQUEST);
                throw exception;
            } catch (Exception exception) {
                generateFailureResponse(transaction, event, BusinessConstants.ERROR_500, BusinessConstants.INTERNAL_SERVER_ERROR,
                        null, exception.getMessage());
                transaction.setResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR);
                throw new ValidationException("Exception in Validating Business: " + exception.getMessage(),
                        BusinessConstants.ERROR_500, BusinessConstants.INTERNAL_SERVER_ERROR, null);
            }
        }
    }

    private void generateFailureResponse(MessageTransaction transaction, CheckB2BIdEvent event, String errorCode,
                                         String errorMsg, String errorField, String message) {
        logger.error("Generating the Failure response for Check B2B ID Request : {} - {} - {}", errorCode,
                errorMsg, message);
        CheckB2BIdResponse response = responseMapper.generateFailureCheckB2BResponse(event.getRequest(),
                errorCode, errorMsg, errorField);
        event.setResponse(response);
        transaction.setResponseMessage(errorMsg);
    }

    @Override
    public EventType getEventType() {
        return EventType.REQ_CHECK_BUSINESS;
    }

    @Override
    public void handleTransactionErrors(MessageTransaction transaction, TransactionError txnError) {
        logger.info("Handling Transaction Errors : {} - {}", txnError.getErrorCode(), txnError.getErrorMsg());
        if (transaction.getRequest() instanceof CheckB2BIdEvent event) {
            generateFailureResponse(transaction, event, txnError.getErrorCode(), txnError.getErrorMsg(),
                    txnError.getErrorField(), "Transaction Error occurred");
            transaction.setResponseStatus(HttpStatus.BAD_REQUEST);
        }
    }
}
