package org.fourstack.business.processor;

import lombok.RequiredArgsConstructor;
import org.fourstack.business.processor.inbound.B2BIdTransactionInboundProcessor;
import org.fourstack.business.dao.service.B2BIdDataService;
import org.fourstack.business.entity.B2BIdentifierEntity;
import org.fourstack.business.entity.event.B2BIdRegisterEvent;
import org.fourstack.business.entity.event.Message;
import org.fourstack.business.mapper.ResponseMapper;
import org.fourstack.business.model.B2BIdRegisterRequest;
import org.fourstack.business.model.B2BIdRegisterResponse;
import org.fourstack.business.model.Head;
import org.fourstack.business.model.MessageTransaction;
import org.fourstack.business.service.HttpClientService;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service("b2bIdMessageProcessor")
@RequiredArgsConstructor(onConstructor_ = @Lazy)
public class B2BMessageProcessor implements MessageProcessor {
    private final B2BIdTransactionInboundProcessor inboundProcessor;
    private final B2BIdDataService b2BIdDataService;
    private final HttpClientService httpClientService;
    private final ResponseMapper responseMapper;

    @Override
    public MessageTransaction transformAndValidate(Message<?,?> message) {
        return inboundProcessor.executeProcess(message);
    }

    @Override
    public void executeBusinessTransactions(MessageTransaction transaction) {
        if (transaction.getRequest() instanceof B2BIdRegisterEvent event) {
            B2BIdRegisterRequest request = event.getRequest();
            String requesterB2BId = request.getOnboardingB2BIds().getRequesterB2BId();
            Optional<B2BIdentifierEntity> b2BIdentifierEntity = b2BIdDataService.retrieveB2BId(requesterB2BId);
            if (b2BIdentifierEntity.isPresent()) {
                Head head = request.getCommonData().getHead();
                B2BIdentifierEntity entity = b2BIdentifierEntity.get();
                b2BIdDataService.createB2BIdEntities(entity.getBusinessRole(), head.getAiId(), head.getOuId(),
                        entity.getOrgId(), request.getOnboardingB2BIds(), request.getRegB2BIds().getIds());
            }

            generateSuccessResponse(transaction, event);
        }
    }

    private void generateSuccessResponse(MessageTransaction transaction, B2BIdRegisterEvent event) {
        B2BIdRegisterResponse response = responseMapper.generateSuccessB2BResponse(event.getRequest());
        event.setResponse(response);
        transaction.setResponseStatus(HttpStatus.OK);
    }

    @Override
    public void sendOutboundRequest(MessageTransaction transaction) {
        httpClientService.constructAndSendOutboundRequest(transaction);
    }
}
