package org.fourstack.business.processor;

import lombok.RequiredArgsConstructor;
import org.fourstack.business.dao.service.AiOrgMapEntityService;
import org.fourstack.business.dao.service.SearchIdentifierService;
import org.fourstack.business.processor.inbound.BusinessTransactionInboundProcessor;
import org.fourstack.business.dao.service.B2BIdDataService;
import org.fourstack.business.dao.service.BusinessEntityDataService;
import org.fourstack.business.dao.service.BusinessIdentifierDataService;
import org.fourstack.business.dao.service.OrgEntityDataService;
import org.fourstack.business.entity.business.BusinessEntity;
import org.fourstack.business.entity.event.BusinessEvent;
import org.fourstack.business.entity.event.Message;
import org.fourstack.business.mapper.ResponseMapper;
import org.fourstack.business.model.BusinessRegisterResponse;
import org.fourstack.business.model.Head;
import org.fourstack.business.model.Institute;
import org.fourstack.business.model.MessageTransaction;
import org.fourstack.business.service.HttpClientService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service("businessMessageProcessor")
@RequiredArgsConstructor(onConstructor_ = @Lazy)
public class BusinessMessageProcessor implements MessageProcessor {
    private static final Logger logger = LoggerFactory.getLogger(BusinessMessageProcessor.class);
    private final BusinessTransactionInboundProcessor businessInboundProcessor;
    private final BusinessEntityDataService businessEntityDataService;
    private final AiOrgMapEntityService aiOrgMapEntityService;
    private final OrgEntityDataService orgEntityDataService;
    private final BusinessIdentifierDataService identifierDataService;
    private final B2BIdDataService b2BIdDataService;
    private final SearchIdentifierService searchIdentifierService;
    private final HttpClientService httpClientService;
    private final ResponseMapper responseMapper;

    @Override
    public MessageTransaction transformAndValidate(Message<?, ?> message) {
        return businessInboundProcessor.executeProcess(message);
    }

    @Override
    public void executeBusinessTransactions(MessageTransaction transaction) {
        if (transaction.getRequest() instanceof BusinessEvent event) {
            logger.info("Executing the Business Transactions on BusinessRegisterRequest");
            BusinessEntity businessEntity = businessEntityDataService.createBusinessEntity(event.getRequest());
            Head head = businessEntity.getHead();
            aiOrgMapEntityService.createAiOrgMapEntity(businessEntity, head.getAiId());
            orgEntityDataService.createOrgIdEntity(businessEntity, head.getAiId(), businessEntity.getTxn().getId());
            Institute institute = businessEntity.getInstitute();
            identifierDataService.createBusinessIdentifier(businessEntity.getBusinessRole(), head.getAiId(),
                    institute.getObjectId(), institute.getPrimaryIdentifier());
            identifierDataService.createBusinessIdentifiers(businessEntity.getBusinessRole(), head.getAiId(),
                    institute.getObjectId(), institute.getOtherIdentifiers());
            b2BIdDataService.createB2BIdEntity(event.getRequest(), businessEntity.getBusinessRole());
            searchIdentifierService.createSearchIdentifiers(businessEntity.getInstitute());

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
        logger.info("Calling HttpClientService for sending outbound request on BusinessRegisterRequest");
        httpClientService.constructAndSendOutboundRequest(transaction);
    }
}
