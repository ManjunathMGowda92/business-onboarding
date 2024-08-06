package org.fourstack.business.processor;

import lombok.RequiredArgsConstructor;
import org.fourstack.business.processor.inbound.BusinessTransactionInboundProcessor;
import org.fourstack.business.dao.service.B2BIdDataService;
import org.fourstack.business.dao.service.BusinessEntityDataService;
import org.fourstack.business.dao.service.BusinessIdentifierDataService;
import org.fourstack.business.dao.service.OrgEntityDataService;
import org.fourstack.business.entity.BusinessEntity;
import org.fourstack.business.entity.event.BusinessEvent;
import org.fourstack.business.entity.event.Message;
import org.fourstack.business.mapper.ResponseMapper;
import org.fourstack.business.model.BusinessRegisterResponse;
import org.fourstack.business.model.Head;
import org.fourstack.business.model.Institute;
import org.fourstack.business.model.MessageTransaction;
import org.fourstack.business.service.HttpClientService;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service("businessMessageProcessor")
@RequiredArgsConstructor(onConstructor_ = @Lazy)
public class BusinessMessageProcessor implements MessageProcessor {
    private final BusinessTransactionInboundProcessor businessInboundProcessor;
    private final BusinessEntityDataService businessEntityDataService;
    private final OrgEntityDataService orgEntityDataService;
    private final BusinessIdentifierDataService identifierDataService;
    private final B2BIdDataService b2BIdDataService;
    private final HttpClientService httpClientService;
    private final ResponseMapper responseMapper;

    @Override
    public MessageTransaction transformAndValidate(Message<?, ?> message) {
        return businessInboundProcessor.executeProcess(message);
    }

    @Override
    public void executeBusinessTransactions(MessageTransaction transaction) {
        if (transaction.getRequest() instanceof BusinessEvent event) {
            BusinessEntity businessEntity = businessEntityDataService.createBusinessEntity(event.getRequest());
            orgEntityDataService.createOrgIdEntity(businessEntity);
            Institute institute = businessEntity.getInstitute();
            Head head = businessEntity.getHead();
            identifierDataService.createBusinessIdentifier(businessEntity.getBusinessRole(), head.getAiId(),
                    institute.getObjectId(), institute.getPrimaryIdentifier());
            identifierDataService.createBusinessIdentifiers(businessEntity.getBusinessRole(), head.getAiId(),
                    institute.getObjectId(), institute.getOtherIdentifiers());
            b2BIdDataService.createB2BIdEntity(event.getRequest(), businessEntity.getBusinessRole());

            generateSuccessResponse(transaction, event);
        }
    }

    private void generateSuccessResponse(MessageTransaction transaction, BusinessEvent event) {
        BusinessRegisterResponse response = responseMapper.generateSuccessBusinessResponse(event.getRequest());
        event.setResponse(response);
        transaction.setResponseStatus(HttpStatus.OK);
    }

    @Override
    public void sendOutboundRequest(MessageTransaction transaction) {
        httpClientService.constructAndSendOutboundRequest(transaction);
    }
}
