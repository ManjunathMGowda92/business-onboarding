package org.fourstack.business.processor.inbound;

import org.fourstack.business.config.ApplicationConfig;
import org.fourstack.business.dao.service.TransactionDataService;
import org.fourstack.business.entity.event.Message;
import org.fourstack.business.entity.event.SearchBusinessEvent;
import org.fourstack.business.enums.EventType;
import org.fourstack.business.enums.TransactionType;
import org.fourstack.business.mapper.EntityMapper;
import org.fourstack.business.mapper.ResponseMapper;
import org.fourstack.business.model.Acknowledgement;
import org.fourstack.business.model.CommonRequestData;
import org.fourstack.business.model.MessageTransaction;
import org.fourstack.business.model.SearchBusinessRequest;
import org.fourstack.business.model.SearchBusinessResponse;
import org.fourstack.business.model.Transaction;
import org.fourstack.business.model.TransactionError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class SearchBusinessInboundProcessor extends DefaultTransactionInboundProcessor{
    private static final Logger logger = LoggerFactory.getLogger(SearchBusinessInboundProcessor.class);
    private final EntityMapper entityMapper;
    private final ResponseMapper responseMapper;

    protected SearchBusinessInboundProcessor(TransactionDataService transactionDataService,
                                             ApplicationConfig applicationConfig, EntityMapper entityMapper, ResponseMapper responseMapper) {
        super(transactionDataService, applicationConfig);
        this.entityMapper = entityMapper;
        this.responseMapper = responseMapper;
    }

    @Override
    public MessageTransaction transform(Message<?, ?> message) {
        if (message.getRequest() instanceof SearchBusinessRequest request) {
            logger.info("Transforming the Search Business Request message");
            return constructTransaction(message, request);
        }
        return null;
    }

    private MessageTransaction constructTransaction(Message<?,?> message, SearchBusinessRequest request) {
        SearchBusinessEvent event = new SearchBusinessEvent();
        event.setRequest(request);
        CommonRequestData commonData = request.getCommonData();
        Transaction txn = commonData.getTxn();
        MessageTransaction transaction = entityMapper.constructTransaction(txn.getId(), commonData.getHead().getMsgId(),
                getEventType(), TransactionType.SEARCH, txn.getTs(), event);
        if (message.getAck() instanceof Acknowledgement acknowledgement) {
            transaction.setAcknowledgement(acknowledgement);
        }
        return transaction;
    }

    @Override
    public void validate(MessageTransaction transaction) {
        logger.info("Validating the Search Business Request");

    }

    @Override
    public EventType getEventType() {
        return EventType.REQ_SEARCH_BUSINESS;
    }

    @Override
    public void handleTransactionErrors(MessageTransaction transaction, TransactionError txnError) {
        logger.info("Handling Transaction Errors : {} - {}", txnError.getErrorCode(), txnError.getErrorMsg());
        if (transaction.getRequest() instanceof SearchBusinessEvent event) {
            SearchBusinessResponse response = responseMapper.generateFailureSearchBusinessResponse(event.getRequest(),
                    txnError.getErrorCode(), txnError.getErrorMsg(), txnError.getErrorField());
            event.setResponse(response);
            transaction.setResponseStatus(HttpStatus.BAD_REQUEST);
            transaction.setResponseMessage("Transaction Error occurred");
        }
    }
}
