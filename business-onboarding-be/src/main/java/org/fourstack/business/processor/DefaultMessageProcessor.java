package org.fourstack.business.processor;

import lombok.RequiredArgsConstructor;
import org.fourstack.business.model.MessageTransaction;
import org.fourstack.business.service.HttpClientService;
import org.springframework.context.annotation.Lazy;

@RequiredArgsConstructor(onConstructor_ = @Lazy)
public abstract class DefaultMessageProcessor implements MessageProcessor{
    protected final HttpClientService httpClientService;
    @Override
    public void sendOutboundRequest(MessageTransaction transaction) {
        httpClientService.constructAndSendOutboundRequest(transaction);
    }
}
