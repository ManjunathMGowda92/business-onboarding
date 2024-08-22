package org.fourstack.business.processor.inbound;

import org.fourstack.business.config.ApplicationConfig;
import org.fourstack.business.constants.BusinessConstants;
import org.fourstack.business.dao.service.TransactionDataService;
import org.fourstack.business.entity.TransactionEntity;
import org.fourstack.business.entity.event.Message;
import org.fourstack.business.enums.ErrorScenarioCode;
import org.fourstack.business.enums.TransactionStatus;
import org.fourstack.business.enums.TransactionSubStatus;
import org.fourstack.business.enums.TransactionType;
import org.fourstack.business.exception.InvalidInputException;
import org.fourstack.business.exception.InvalidTransactionException;
import org.fourstack.business.exception.ValidationException;
import org.fourstack.business.model.MessageTransaction;
import org.fourstack.business.model.TransactionError;
import org.fourstack.business.utils.BusinessUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import java.time.Instant;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public abstract class DefaultTransactionInboundProcessor implements TransactionInboundProcessor {
    private static final Logger logger = LoggerFactory.getLogger(DefaultTransactionInboundProcessor.class);
    protected final TransactionDataService transactionDataService;
    protected final ApplicationConfig applicationConfig;

    protected DefaultTransactionInboundProcessor(TransactionDataService transactionDataService,
                                                 ApplicationConfig applicationConfig) {
        this.transactionDataService = transactionDataService;
        this.applicationConfig = applicationConfig;
    }

    @Override
    public MessageTransaction executeProcess(Message<?, ?> message) {
        MessageTransaction transaction = null;
        try {
            transaction = transform(message);
            validateAndPersistTransaction(transaction);
            validate(transaction);
            transaction.setResponseStatus(HttpStatus.OK);
            transactionDataService.updateTransactionEntity(transaction, TransactionStatus.IN_PROGRESS,
                    TransactionSubStatus.IN_PROGRESS, Collections.emptyList());
        } catch (ValidationException exception) {
            persistValidationErrorTransaction(exception, transaction);
        } catch (InvalidTransactionException exception) {
            persistErrorTransaction(exception, transaction);
        } catch (Exception exception) {
            transaction = persistInvalidTransaction(message, exception.getMessage());
        }
        return transaction;
    }

    private void persistValidationErrorTransaction(ValidationException exception, MessageTransaction transaction) {
        if (Objects.nonNull(transaction)) {
            transaction.setResponseStatus(HttpStatus.BAD_REQUEST);
            TransactionError txnError = BusinessUtil.convertToTxnError(exception);
            List<TransactionError> txnErrors = BusinessUtil.isCollectionNotNullOrEmpty(transaction.getTxnErrors())
                    ? transaction.getTxnErrors() : new ArrayList<>();
            txnErrors.add(txnError);
            transaction.setTxnErrors(txnErrors);
            createOrUpdateTransaction(transaction, TransactionStatus.FAILED,
                    TransactionSubStatus.BUSINESS_VALIDATION_FAILURE);
        } else {
            throw new InvalidInputException("Invalid message received", ErrorScenarioCode.GEN_0007.getErrorCode(),
                    ErrorScenarioCode.GEN_0007.getErrorMsg());
        }
    }

    private MessageTransaction persistInvalidTransaction(Message<?, ?> message, String errorMsg) {
        MessageTransaction transaction = generateErrorTransaction(message, errorMsg);
        transactionDataService.createTransactionEntity(transaction, TransactionStatus.INVALID,
                TransactionSubStatus.INVALID_TRANSACTION_RECEIVED);
        transactionDataService.auditInboundTransaction(transaction);
        return transaction;
    }

    private void persistErrorTransaction(InvalidTransactionException exception, MessageTransaction transaction) {
        if (Objects.nonNull(transaction)) {
            transaction.setResponseStatus(HttpStatus.BAD_REQUEST);
            TransactionError txnError = BusinessUtil.convertToTxnError(exception);
            handleTransactionErrors(transaction, txnError);
            transaction.setTxnErrors(List.of(txnError));
            createOrUpdateTransaction(transaction, TransactionStatus.FAILED, TransactionSubStatus.TXN_VALIDATION_FAILURE);
        } else {
            throw new InvalidInputException("Invalid message received", ErrorScenarioCode.GEN_0007.getErrorCode(),
                    ErrorScenarioCode.GEN_0007.getErrorMsg());
        }
    }

    private MessageTransaction generateErrorTransaction(Message<?, ?> message, String errorMsg) {
        MessageTransaction transaction;
        transaction = new MessageTransaction(BusinessUtil.generateAlphaNumericID(32, "ERR-TXN"),
                BusinessUtil.generateAlphaNumericID(32, "MSG"), TransactionType.INVALID, getEventType(),
                BusinessUtil.getCurrentTimeStamp());
        transaction.setRequest(message);
        transaction.setResponseMessage(errorMsg);
        transaction.setResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        TransactionError txnError = BusinessUtil.generateTxnError(errorMsg, ErrorScenarioCode.GEN_0000.getErrorCode(),
                ErrorScenarioCode.GEN_0000.getErrorMsg(), null);
        List<TransactionError> errors = new ArrayList<>();
        errors.add(txnError);
        transaction.setTxnErrors(errors);
        return transaction;
    }

    private void createOrUpdateTransaction(MessageTransaction transaction, TransactionStatus status,
                                           TransactionSubStatus subStatus) {
        if (BusinessUtil.isNotNull(transaction)) {
            String transactionId = transaction.getTransactionId();
            Optional<TransactionEntity> optionalTransactionEntity = transactionDataService.retrieveTransaction(transactionId);
            if (optionalTransactionEntity.isEmpty()) {
                logger.info("{} - Creating new transaction for id: {}", this.getClass().getSimpleName(), transactionId);
                transactionDataService.createTransactionEntity(transaction, TransactionStatus.CREATED, subStatus);
                transactionDataService.auditInboundTransaction(transaction);
            } else {
                transactionDataService.updateTransactionEntity(optionalTransactionEntity.get(), status,
                        subStatus, transaction.getTxnErrors());
                transactionDataService.auditInboundTransaction(transaction);
            }
        } else {
            throw new InvalidInputException("Null Object received", ErrorScenarioCode.GEN_0000.getErrorCode(),
                    ErrorScenarioCode.GEN_0000.getErrorMsg());
        }
    }

    @Override
    public void validateAndPersistTransaction(MessageTransaction transaction) {
        validateTransactionTime(transaction);
        String transactionId = transaction.getTransactionId();
        Optional<TransactionEntity> optionalTransactionEntity = transactionDataService.retrieveTransaction(transactionId);
        if (optionalTransactionEntity.isEmpty()) {
            logger.info("{} - Creating new transaction for id: {}", this.getClass().getSimpleName(), transactionId);
            transactionDataService.createTransactionEntity(transaction, TransactionStatus.CREATED, TransactionSubStatus.NEW);
            transactionDataService.auditInboundTransaction(transaction);
        } else {
            throw new InvalidTransactionException("Transaction already exist: " + transactionId, BusinessConstants.TXN_ID,
                    ErrorScenarioCode.TXN_0002);
        }
    }

    private void validateTransactionTime(MessageTransaction transaction) {
        try {
            String transactionTimeStamp = transaction.getTransactionTimeStamp();
            Instant transactionTime = Instant.parse(transactionTimeStamp);
            long timeDifference = BusinessUtil.getTimeDifference(transactionTime);
            int configuredTime = getConfiguredTimeOutValue();
            if (timeDifference >= configuredTime) {
                throw new InvalidTransactionException("Transaction time is beyond server time : " + timeDifference,
                        BusinessConstants.TXN_ID, ErrorScenarioCode.TXN_0004);
            }
        } catch (DateTimeParseException exception) {
            throw new InvalidTransactionException("Exception in parsing the transaction time",
                    BusinessConstants.TXN_ID, ErrorScenarioCode.TXN_0004);
        }
    }

    private int getConfiguredTimeOutValue() {
        Map<String, String> config = applicationConfig.getConfig();
        if (BusinessUtil.isNotNull(config)) {
            String configuredTime = config.get("transaction-time-out");
            return BusinessUtil.getIntValue(configuredTime, BusinessConstants.DEFAULT_TXN_TIME_OUT);
        }
        return BusinessConstants.DEFAULT_TXN_TIME_OUT;
    }
}
