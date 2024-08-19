package org.fourstack.business.processor;

import lombok.RequiredArgsConstructor;
import org.fourstack.business.entity.event.Message;
import org.fourstack.business.model.MessageTransaction;
import org.fourstack.business.processor.inbound.CheckBusinessInboundProcessor;
import org.fourstack.business.service.HttpClientService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Service("checkBusinessMessageProcessor")
@RequiredArgsConstructor(onConstructor_ = @Lazy)
public class CheckBusinessMessageProcessor implements MessageProcessor {
    private static final Logger logger = LoggerFactory.getLogger(CheckBusinessMessageProcessor.class);
    private final CheckBusinessInboundProcessor inboundProcessor;
    private final HttpClientService httpClientService;

    @Override
    public MessageTransaction transformAndValidate(Message<?, ?> message) {
        return inboundProcessor.executeProcess(message);
    }

    @Override
    public void executeBusinessTransactions(MessageTransaction transaction) {
        logger.info("No Business Transactions Required for Check Business Request, as it is a INQUIRY event");
    }

    @Override
    public void sendOutboundRequest(MessageTransaction transaction) {
        logger.info("Calling HttpClientService for sending outbound request on CheckBusiness");
        httpClientService.constructAndSendOutboundRequest(transaction);
    }
}
