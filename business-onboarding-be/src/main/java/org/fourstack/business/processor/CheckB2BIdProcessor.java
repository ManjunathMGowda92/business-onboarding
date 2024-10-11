package org.fourstack.business.processor;

import lombok.RequiredArgsConstructor;
import org.fourstack.business.entity.event.Message;
import org.fourstack.business.model.MessageTransaction;
import org.fourstack.business.processor.inbound.CheckB2BIdInboundProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Service("checkB2bProcessor")
@RequiredArgsConstructor(onConstructor_ = @Lazy)
public class CheckB2BIdProcessor implements MessageProcessor {
    private static final Logger logger = LoggerFactory.getLogger(CheckB2BIdProcessor.class);
    private final CheckB2BIdInboundProcessor inboundProcessor;

    @Override
    public MessageTransaction transformAndValidate(Message<?, ?> message) {
        logger.info("Calling Inbound processor to transform and validate CheckB2BID Request");
        return inboundProcessor.executeProcess(message);
    }

    @Override
    public void executeBusinessTransactions(MessageTransaction transaction) {

    }

    @Override
    public void sendOutboundRequest(MessageTransaction transaction) {

    }
}
