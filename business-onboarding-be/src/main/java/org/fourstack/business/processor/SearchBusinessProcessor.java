package org.fourstack.business.processor;

import lombok.RequiredArgsConstructor;
import org.fourstack.business.constants.BusinessConstants;
import org.fourstack.business.dao.service.DataRetriever;
import org.fourstack.business.entity.event.Message;
import org.fourstack.business.entity.event.SearchBusinessEvent;
import org.fourstack.business.exception.ValidationException;
import org.fourstack.business.mapper.ResponseMapper;
import org.fourstack.business.model.EntityInfo;
import org.fourstack.business.model.MessageTransaction;
import org.fourstack.business.model.SearchBusinessResponse;
import org.fourstack.business.processor.inbound.SearchBusinessInboundProcessor;
import org.fourstack.business.service.HttpClientService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("searchBusinessProcessor")
@RequiredArgsConstructor(onConstructor_ = @Lazy)
public class SearchBusinessProcessor implements MessageProcessor {
    private static final Logger logger = LoggerFactory.getLogger(SearchBusinessProcessor.class);
    private final SearchBusinessInboundProcessor inboundProcessor;
    private final DataRetriever searchRetriever;
    private final HttpClientService httpClientService;
    private final ResponseMapper responseMapper;

    @Override
    public MessageTransaction transformAndValidate(Message<?, ?> message) {
        logger.info("Calling Inbound Processor to transform and validate the SearchBusiness");
        return inboundProcessor.executeProcess(message);
    }

    @Override
    public void executeBusinessTransactions(MessageTransaction transaction) {
        if (transaction.getRequest() instanceof SearchBusinessEvent event) {
            logger.info("Calling Business Transaction execution on SearchBusiness");
            try {
                List<EntityInfo> entityInfoList = searchRetriever.retrieveSearchResults(event.getRequest().getSearch());
                generateSuccessResponse(transaction, event, entityInfoList);
            } catch (ValidationException exception) {
                logger.error("Validation exception occurred for Search Business : {} - {} - {}", exception.getErrorCode(),
                        exception.getErrorMsg(), exception.getErrorField());
                generateFailureResponse(transaction, event, exception.getErrorCode(), exception.getErrorMsg(),
                        exception.getErrorField());
                transaction.setResponseStatus(HttpStatus.BAD_REQUEST);
            } catch (Exception exception) {
                logger.error("Exception occurred for Search Business : {}", exception.getMessage());
                generateFailureResponse(transaction, event, BusinessConstants.ERROR_500, BusinessConstants.INTERNAL_SERVER_ERROR,
                        null);
                transaction.setResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
    }

    private void generateFailureResponse(MessageTransaction transaction, SearchBusinessEvent event, String errorCode,
                                         String errorMsg, String errorField) {
        SearchBusinessResponse response = responseMapper.generateFailureSearchBusinessResponse(event.getRequest(),
                errorCode, errorMsg, errorField);
        event.setResponse(response);
        transaction.setResponseMessage(errorMsg);
    }

    private void generateSuccessResponse(MessageTransaction transaction, SearchBusinessEvent event,
                                         List<EntityInfo> entityInfoList) {
        SearchBusinessResponse response = responseMapper.generateSuccessSearchResponse(event.getRequest(), entityInfoList);
        event.setResponse(response);
        transaction.setResponseStatus(HttpStatus.OK);

    }

    @Override
    public void sendOutboundRequest(MessageTransaction transaction) {
        logger.info("Calling HttpClientService for sending outbound request on SearchBusiness");
        httpClientService.constructAndSendOutboundRequest(transaction);
    }
}
