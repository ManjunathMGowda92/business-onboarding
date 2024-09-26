package org.fourstack.business.processor.inbound;

import org.fourstack.business.config.ApplicationConfig;
import org.fourstack.business.constants.BusinessConstants;
import org.fourstack.business.dao.service.TransactionDataService;
import org.fourstack.business.entity.event.CheckInstituteEvent;
import org.fourstack.business.entity.event.Message;
import org.fourstack.business.enums.ErrorScenarioCode;
import org.fourstack.business.enums.EventType;
import org.fourstack.business.enums.TransactionType;
import org.fourstack.business.exception.InvalidInputException;
import org.fourstack.business.exception.ValidationException;
import org.fourstack.business.mapper.EntityMapper;
import org.fourstack.business.mapper.ResponseMapper;
import org.fourstack.business.model.Acknowledgement;
import org.fourstack.business.model.CheckBusinessRequest;
import org.fourstack.business.model.CheckBusinessResponse;
import org.fourstack.business.model.CheckInstituteResponse;
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
public class CheckBusinessInboundProcessor extends DefaultTransactionInboundProcessor {
    private static final Logger logger = LoggerFactory.getLogger(CheckBusinessInboundProcessor.class);

    private final EntityMapper entityMapper;
    private final ResponseMapper responseMapper;
    private final BusinessValidator businessValidator;

    protected CheckBusinessInboundProcessor(TransactionDataService transactionDataService,
                                            ApplicationConfig applicationConfig, EntityMapper entityMapper,
                                            ResponseMapper responseMapper, BusinessValidator businessValidator) {
        super(transactionDataService, applicationConfig);
        this.entityMapper = entityMapper;
        this.responseMapper = responseMapper;
        this.businessValidator = businessValidator;
    }

    @Override
    public MessageTransaction transform(Message<?, ?> message) {
        if (message.getRequest() instanceof CheckBusinessRequest request) {
            logger.info("Transforming the Check Business Message");
            return constructTransaction(message, request);
        }
        logger.error("Invalid request received for processing");
        throw new InvalidInputException("Invalid message received", ErrorScenarioCode.GEN_0007.getErrorCode(),
                ErrorScenarioCode.GEN_0007.getErrorMsg());
    }

    private MessageTransaction constructTransaction(Message<?, ?> message, CheckBusinessRequest request) {
        CheckInstituteEvent event = new CheckInstituteEvent();
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

    @Override
    public void validate(MessageTransaction transaction) {
        if (transaction.getRequest() instanceof CheckInstituteEvent event) {
            try {
                CheckInstituteResponse instituteResponse = businessValidator.checkBusinessValidations(event.getRequest());
                CheckBusinessResponse businessResponse = responseMapper.generateSuccessCheckBusinessResponse(event.getRequest(), instituteResponse);
                event.setResponse(businessResponse);
                transaction.setResponseStatus(HttpStatus.OK);
            } catch (ValidationException exception) {
                setErrorDetails(transaction, event, exception.getErrorCode(), exception.getErrorMsg(),
                        exception.getErrorField(), exception.getMessage());
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

    private void setErrorDetails(MessageTransaction transaction, CheckInstituteEvent event, String errorCode,
                                 String errorMsg, String errorField, String message) {
        logger.error("Exception occurred while validating CheckBusinessRequest : {} - {} -{}",
                errorCode, errorMsg, message);
        CheckBusinessResponse response = responseMapper.generateFailureCheckBusinessResponse(event.getRequest(),
                errorCode, errorMsg, errorField);
        event.setResponse(response);
        transaction.setResponseMessage(message);
    }

    @Override
    public EventType getEventType() {
        return EventType.REQ_CHECK_BUSINESS;
    }

    @Override
    public void handleTransactionErrors(MessageTransaction transaction, TransactionError txnError) {
        logger.info("Handling Transaction Errors : {} - {}", txnError.getErrorCode(), txnError.getErrorMsg());
        if (transaction.getRequest() instanceof CheckInstituteEvent event) {
            CheckBusinessResponse response = responseMapper.generateFailureCheckBusinessResponse(event.getRequest(),
                    txnError.getErrorCode(), txnError.getErrorMsg(), txnError.getErrorField());
            event.setResponse(response);
            transaction.setResponseStatus(HttpStatus.BAD_REQUEST);
            transaction.setResponseMessage("Transaction Error occurred");
        }
    }
}
