package org.fourstack.business.processor;

import lombok.RequiredArgsConstructor;
import org.fourstack.business.dao.service.DataRetriever;
import org.fourstack.business.entity.event.CheckB2BIdEvent;
import org.fourstack.business.entity.event.Message;
import org.fourstack.business.mapper.ResponseMapper;
import org.fourstack.business.model.B2BAvailabilityResponse;
import org.fourstack.business.model.CheckB2BIdRequest;
import org.fourstack.business.model.CheckB2BIdResponse;
import org.fourstack.business.model.MessageTransaction;
import org.fourstack.business.processor.inbound.CheckB2BIdInboundProcessor;
import org.fourstack.business.service.HttpClientService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("checkB2bProcessor")
@RequiredArgsConstructor(onConstructor_ = @Lazy)
public class CheckB2BIdProcessor implements MessageProcessor {
    private static final Logger logger = LoggerFactory.getLogger(CheckB2BIdProcessor.class);
    private final CheckB2BIdInboundProcessor inboundProcessor;
    private final DataRetriever dataRetriever;
    private final ResponseMapper responseMapper;
    private final HttpClientService httpClientService;

    @Override
    public MessageTransaction transformAndValidate(Message<?, ?> message) {
        logger.info("Calling Inbound processor to transform and validate CheckB2BID Request");
        return inboundProcessor.executeProcess(message);
    }

    @Override
    public void executeBusinessTransactions(MessageTransaction transaction) {
        if (transaction.getRequest() instanceof CheckB2BIdEvent event) {
            logger.info("Calling Business Transaction execution on Check B2BID Request");
            List<B2BAvailabilityResponse> responseList =
                    dataRetriever.retrieveB2BIdAvailableStatus(event.getRequest().getCheckB2BIds());
            CheckB2BIdResponse response = responseMapper.generateSuccessCheckB2BResponse(event.getRequest(), responseList);
            event.setResponse(response);
            transaction.setResponseStatus(HttpStatus.OK);
        }
    }

    @Override
    public void sendOutboundRequest(MessageTransaction transaction) {
        logger.info("Calling HttpClient Service for sending outbound request on CheckB2BId");
        httpClientService.constructAndSendOutboundRequest(transaction);
    }
}
