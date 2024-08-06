package org.fourstack.business.processor.inbound;

import org.fourstack.business.config.ApplicationConfig;
import org.fourstack.business.constants.BusinessConstants;
import org.fourstack.business.dao.service.TransactionDataService;
import org.fourstack.business.entity.event.BusinessEvent;
import org.fourstack.business.entity.event.Message;
import org.fourstack.business.enums.ErrorScenarioCode;
import org.fourstack.business.enums.EventType;
import org.fourstack.business.enums.TransactionType;
import org.fourstack.business.exception.InvalidInputException;
import org.fourstack.business.exception.ValidationException;
import org.fourstack.business.mapper.EntityMapper;
import org.fourstack.business.mapper.ResponseMapper;
import org.fourstack.business.model.Acknowledgement;
import org.fourstack.business.model.BusinessRegisterRequest;
import org.fourstack.business.model.BusinessRegisterResponse;
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
public class BusinessTransactionInboundProcessor extends DefaultTransactionInboundProcessor {
    private static final Logger logger = LoggerFactory.getLogger(BusinessTransactionInboundProcessor.class);

    private final EntityMapper entityMapper;
    private final ResponseMapper responseMapper;
    private final BusinessValidator businessValidator;

    public BusinessTransactionInboundProcessor(TransactionDataService transactionDataService, ApplicationConfig appConfig,
                                               EntityMapper entityMapper, ResponseMapper responseMapper,
                                               BusinessValidator businessValidator) {
        super(transactionDataService, appConfig);
        this.entityMapper = entityMapper;
        this.responseMapper = responseMapper;
        this.businessValidator = businessValidator;
    }

    @Override
    public MessageTransaction transform(Message<?, ?> message) {
        if (message.getRequest() instanceof BusinessRegisterRequest request) {
            logger.info("{} - Transforming the Business Message", this.getClass().getSimpleName());
            return constructTransaction(message, request);
        }
        logger.error("{} - Invalid request receive in the Business Message", this.getClass().getSimpleName());
        throw new InvalidInputException("Invalid message received", ErrorScenarioCode.GEN_0007.getErrorCode(),
                ErrorScenarioCode.GEN_0007.getErrorMsg());
    }

    @Override
    public void validate(MessageTransaction transaction) {
        if (transaction.getRequest() instanceof BusinessEvent event) {
            try {
                businessValidator.businessRegisterValidations(event.getRequest());
            } catch (ValidationException exception) {
                setErrorDetails(transaction, event, exception.getErrorCode(),
                        exception.getErrorMsg(), exception.getErrorField(), exception.getMessage());
                transaction.setResponseStatus(HttpStatus.BAD_REQUEST);
                throw exception;
            } catch (Exception exception) {
                setErrorDetails(transaction, event, BusinessConstants.ERROR_500, BusinessConstants.INTERNAL_SERVER_ERROR,
                        null, exception.getMessage());
                transaction.setResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR);
                throw new ValidationException("Exception in Validating Business: " + exception.getMessage(),
                        BusinessConstants.ERROR_500, BusinessConstants.INTERNAL_SERVER_ERROR, null);
            }
        }
    }

    private void setErrorDetails(MessageTransaction transaction, BusinessEvent event, String errorCode, String errorMsg,
                                 String errorField, String message) {
        BusinessRegisterResponse response = responseMapper.generateFailureBusinessResponse(event.getRequest(),
                errorCode, errorMsg, errorField);
        event.setResponse(response);
        transaction.setResponseMessage(message);
    }

    @Override
    public EventType getEventType() {
        return EventType.REQ_CREATE_BUSINESS;
    }

    @Override
    public void handleTransactionErrors(MessageTransaction transaction, TransactionError txnError) {
        logger.info("{} - Handling Transaction Errors : {} - {}", this.getClass().getSimpleName(),
                txnError.getErrorCode(), txnError.getErrorMsg());
        if (transaction.getRequest() instanceof BusinessEvent event) {
            BusinessRegisterResponse response = responseMapper.generateFailureBusinessResponse(event.getRequest(),
                    txnError.getErrorCode(), txnError.getErrorMsg(), txnError.getErrorField());
            event.setResponse(response);
            transaction.setResponseStatus(HttpStatus.BAD_REQUEST);
            transaction.setResponseMessage("Transaction Error occurred");
        }
    }

    private MessageTransaction constructTransaction(Message<?, ?> message, BusinessRegisterRequest request) {
        BusinessEvent event = new BusinessEvent();
        event.setRequest(request);
        CommonRequestData commonData = request.getCommonData();
        Transaction txn = commonData.getTxn();
        MessageTransaction transaction = entityMapper.constructTransaction(txn.getId(), commonData.getHead().getMsgId(),
                getEventType(), TransactionType.ENTITY, txn.getTs(), event);
        if (message.getAck() instanceof Acknowledgement acknowledgement) {
            transaction.setAcknowledgement(acknowledgement);
        }
        return transaction;
    }
}
