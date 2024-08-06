package org.fourstack.business.processor.inbound;

import org.fourstack.business.config.ApplicationConfig;
import org.fourstack.business.constants.BusinessConstants;
import org.fourstack.business.dao.service.TransactionDataService;
import org.fourstack.business.entity.event.B2BIdRegisterEvent;
import org.fourstack.business.entity.event.Message;
import org.fourstack.business.enums.ErrorScenarioCode;
import org.fourstack.business.enums.EventType;
import org.fourstack.business.enums.TransactionType;
import org.fourstack.business.exception.InvalidInputException;
import org.fourstack.business.exception.ValidationException;
import org.fourstack.business.mapper.EntityMapper;
import org.fourstack.business.mapper.ResponseMapper;
import org.fourstack.business.model.Acknowledgement;
import org.fourstack.business.model.B2BIdRegisterRequest;
import org.fourstack.business.model.B2BIdRegisterResponse;
import org.fourstack.business.model.CommonRequestData;
import org.fourstack.business.model.MessageTransaction;
import org.fourstack.business.model.Transaction;
import org.fourstack.business.model.TransactionError;
import org.fourstack.business.validator.BusinessValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service("b2bIdMessageConsumer")
public class B2BIdTransactionInboundProcessor extends DefaultTransactionInboundProcessor {
    private static final Logger logger = LoggerFactory.getLogger(B2BIdTransactionInboundProcessor.class);
    private final EntityMapper entityMapper;
    private final BusinessValidator businessValidator;
    private final ResponseMapper responseMapper;

    public B2BIdTransactionInboundProcessor(TransactionDataService transactionDataService, ApplicationConfig appConfig,
                                            EntityMapper entityMapper, BusinessValidator businessValidator,
                                            ResponseMapper responseMapper) {
        super(transactionDataService, appConfig);
        this.entityMapper = entityMapper;
        this.businessValidator = businessValidator;
        this.responseMapper = responseMapper;
    }

    @Override
    public MessageTransaction transform(Message<?, ?> message) {
        if (message.getRequest() instanceof B2BIdRegisterRequest request) {
            logger.info("{} - Transforming the Business Message", this.getClass().getSimpleName());
            return constructTransaction(message, request);
        }
        logger.error("{} - Invalid request receive in the Business Message", this.getClass().getSimpleName());
        throw new InvalidInputException("Invalid message received", ErrorScenarioCode.GEN_0007.getErrorCode(),
                ErrorScenarioCode.GEN_0007.getErrorMsg());
    }

    private MessageTransaction constructTransaction(Message<?, ?> message, B2BIdRegisterRequest request) {
        B2BIdRegisterEvent event = new B2BIdRegisterEvent();
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

    @Override
    public void validate(MessageTransaction transaction) {
        if (transaction.getRequest() instanceof B2BIdRegisterEvent event) {
            try {
                businessValidator.b2bBusinessRegisterValidations(event.getRequest());
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

    private void setErrorDetails(MessageTransaction transaction, B2BIdRegisterEvent event, String errorCode, String errorMsg,
                                 String errorField, String message) {
        B2BIdRegisterResponse response = responseMapper.generateFailureB2BResponse(event.getRequest(),
                errorCode, errorMsg, errorField);
        event.setResponse(response);
        transaction.setResponseMessage(message);
    }

    @Override
    public EventType getEventType() {
        return EventType.REQ_ADD_B2B;
    }

    @Override
    public void handleTransactionErrors(MessageTransaction transaction, TransactionError txnError) {
        logger.info("{} - Handling Transaction Errors : {} - {}", this.getClass().getSimpleName(),
                txnError.getErrorCode(), txnError.getErrorMsg());
        if (transaction.getRequest() instanceof B2BIdRegisterEvent event) {
            B2BIdRegisterResponse response = responseMapper.generateFailureB2BResponse(event.getRequest(),
                    txnError.getErrorCode(), txnError.getErrorMsg(), txnError.getErrorField());
            event.setResponse(response);
            transaction.setRequest(event);
            transaction.setResponseStatus(HttpStatus.BAD_REQUEST);
        }
    }
}
