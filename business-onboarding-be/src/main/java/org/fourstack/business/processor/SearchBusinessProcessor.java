package org.fourstack.business.processor;

import lombok.RequiredArgsConstructor;
import org.fourstack.business.entity.event.Message;
import org.fourstack.business.model.MessageTransaction;
import org.fourstack.business.processor.inbound.SearchBusinessInboundProcessor;
import org.fourstack.business.service.HttpClientService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Service("searchBusinessProcessor")
@RequiredArgsConstructor(onConstructor_ = @Lazy)
public class SearchBusinessProcessor implements MessageProcessor{
    private static final Logger logger = LoggerFactory.getLogger(SearchBusinessProcessor.class);
    private final SearchBusinessInboundProcessor inboundProcessor;
    private final HttpClientService httpClientService;
    @Override
    public MessageTransaction transformAndValidate(Message<?, ?> message) {
        logger.info("Calling Inbound Processor to transform and validate the SearchBusiness");
        return inboundProcessor.executeProcess(message);
    }

    @Override
    public void executeBusinessTransactions(MessageTransaction transaction) {
        logger.info("Calling Business Transaction execution on SearchBusiness");
    }

    @Override
    public void sendOutboundRequest(MessageTransaction transaction) {
        logger.info("Calling HttpClientService for sending outbound request on SearchBusiness");
        httpClientService.constructAndSendOutboundRequest(transaction);
    }
}
