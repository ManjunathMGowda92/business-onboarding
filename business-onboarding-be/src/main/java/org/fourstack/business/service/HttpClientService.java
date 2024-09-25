package org.fourstack.business.service;

import org.fourstack.business.constants.BusinessConstants;
import org.fourstack.business.dao.service.MasterDataService;
import org.fourstack.business.dao.service.TransactionDataService;
import org.fourstack.business.entity.event.B2BIdRegisterEvent;
import org.fourstack.business.entity.event.BusinessEvent;
import org.fourstack.business.entity.event.CheckInstituteEvent;
import org.fourstack.business.entity.event.SearchBusinessEvent;
import org.fourstack.business.enums.ErrorScenarioCode;
import org.fourstack.business.enums.EventWebhookType;
import org.fourstack.business.enums.TransactionStatus;
import org.fourstack.business.enums.TransactionSubStatus;
import org.fourstack.business.exception.HttpFailureException;
import org.fourstack.business.model.B2BIdRegisterResponse;
import org.fourstack.business.model.BusinessRegisterResponse;
import org.fourstack.business.model.CheckBusinessResponse;
import org.fourstack.business.model.Head;
import org.fourstack.business.model.MessageTransaction;
import org.fourstack.business.model.SearchBusinessResponse;
import org.fourstack.business.model.TransactionError;
import org.fourstack.business.model.WebhookRequest;
import org.fourstack.business.utils.BusinessUtil;
import org.fourstack.business.utils.JsonUtilityHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class HttpClientService {
    private static final Logger logger = LoggerFactory.getLogger(HttpClientService.class);
    private final HttpClient httpClient;
    private final MasterDataService masterDataService;
    private final TransactionDataService transactionDataService;

    public HttpClientService(@Qualifier("httpClient") HttpClient httpClient, MasterDataService masterDataService,
                             TransactionDataService transactionDataService) {
        this.httpClient = httpClient;
        this.masterDataService = masterDataService;
        this.transactionDataService = transactionDataService;
    }

    public void constructAndSendOutboundRequest(MessageTransaction transaction) {
        try {
            Object request = transaction.getRequest();
            switch (request) {
                case BusinessEvent businessEvent -> constructAndSendBusinessResponse(businessEvent, transaction);
                case B2BIdRegisterEvent b2bIdEvent -> constructAndSendB2BResponse(b2bIdEvent, transaction);
                case CheckInstituteEvent checkEvent -> constructAndSendResponse(checkEvent, transaction);
                case SearchBusinessEvent searchEvent -> constructAndSendResponse(searchEvent, transaction);
                default ->
                        logger.error("{} - Invalid Object type for outbound request : {}", this.getClass().getSimpleName(), request);
            }
        } catch (Exception exception) {
            logger.error("{} - Http Failure : {}", this.getClass().getSimpleName(), exception.getMessage());
            List<TransactionError> txnErrors = BusinessUtil.isCollectionNotNullOrEmpty(transaction.getTxnErrors())
                    ? transaction.getTxnErrors() : new ArrayList<>();
            TransactionError txnError = BusinessUtil.generateTxnError("Http outbound failure",
                    BusinessConstants.ERROR_500, exception.getMessage(), null);
            txnErrors.add(txnError);
            transaction.setTxnErrors(txnErrors);
            transactionDataService.updateTransactionEntity(transaction, TransactionStatus.OUTBOUND_FAILURE,
                    TransactionSubStatus.OUTBOUND_HTTP_NOT_SENT, txnErrors);
            transactionDataService.auditOutboundTransaction(transaction);
        }
    }

    private void constructAndSendResponse(SearchBusinessEvent searchEvent, MessageTransaction transaction) {
        SearchBusinessResponse response = searchEvent.getResponse();
        Head head = response.getCommonData().getHead();
        WebhookRequest webhookRequest = constructWebhookRequest(response, HttpMethod.POST,
                EventWebhookType.RESP_SEARCH_ENTITY, head.getAiId());
        sendOutboundRequest(webhookRequest, transaction);
    }

    private void constructAndSendResponse(CheckInstituteEvent checkEvent, MessageTransaction transaction) {
        CheckBusinessResponse response = checkEvent.getResponse();
        Head head = response.getCommonData().getHead();
        WebhookRequest webhookRequest = constructWebhookRequest(response, HttpMethod.POST,
                EventWebhookType.RESP_CHECK_ENTITY, head.getAiId());
        sendOutboundRequest(webhookRequest, transaction);
    }

    private void constructAndSendB2BResponse(B2BIdRegisterEvent b2bIdEvent, MessageTransaction transaction) {
        B2BIdRegisterResponse response = b2bIdEvent.getResponse();
        Head head = response.getCommonData().getHead();
        WebhookRequest webhookRequest = constructWebhookRequest(response, HttpMethod.POST, EventWebhookType.RESP_ADD_B2B,
                head.getAiId());
        sendOutboundRequest(webhookRequest, transaction);
    }

    private void constructAndSendBusinessResponse(BusinessEvent businessEvent, MessageTransaction transaction) {
        BusinessRegisterResponse response = businessEvent.getResponse();
        Head head = response.getCommonData().getHead();
        WebhookRequest webhookRequest = constructWebhookRequest(response, HttpMethod.POST,
                EventWebhookType.RESP_CREATE_BUSINESS, head.getAiId());
        sendOutboundRequest(webhookRequest, transaction);
    }

    private WebhookRequest constructWebhookRequest(Object response, HttpMethod methodType,
                                                   EventWebhookType eventType, String aiId) {
        WebhookRequest webhookRequest = new WebhookRequest();
        webhookRequest.setMethodType(methodType);
        Optional<String> webhookUrl = masterDataService.getWebhookUrl(aiId);
        webhookUrl.ifPresentOrElse(url -> webhookRequest.setUrl(url.concat(eventType.getEndPoint())),
                () -> webhookRequest.setUrl(eventType.getEndPoint()));
        try {
            webhookRequest.setRequestBody(JsonUtilityHelper.convertToString(response));
        } catch (Exception exception) {
            webhookRequest.setRequestBody("");
        }
        return webhookRequest;
    }


    private void sendOutboundRequest(WebhookRequest webhookRequest, MessageTransaction transaction) {
        logger.info("Sending outbound request to target URL : {}", webhookRequest.getUrl());
        try {
            logger.info("Outbound Request : {}", webhookRequest.getRequestBody());
            HttpRequest httpRequest = createRequest(webhookRequest.getUrl(), webhookRequest.getMethodType().name(),
                    webhookRequest.getRequestBody());
            httpClient.sendAsync(httpRequest, HttpResponse.BodyHandlers.ofString())
                    .handleAsync((response, exception) -> {
                        if (BusinessUtil.isNotNull(exception)) {
                            logger.info(" Exception in sending response - {}", exception.getMessage());
                            updateTransaction(transaction, HttpStatus.SERVICE_UNAVAILABLE.value());
                        } else {
                            int statusCode = response.statusCode();
                            logger.info("Response status code received from target : {}", statusCode);
                            logger.info("Response received from target : {}", response.body());
                            updateTransaction(transaction, statusCode);
                        }
                        return response;
                    });
        } catch (Exception exception) {
            throw new HttpFailureException("Failure in sending outbound request : " + exception.getMessage());
        }
    }

    private void updateTransaction(MessageTransaction transaction, int statusCode) {
        HttpStatusCode httpStatusCode = HttpStatusCode.valueOf(statusCode);
        List<TransactionError> txnErrors = transaction.getTxnErrors();
        if (httpStatusCode.is2xxSuccessful()) {
            transactionDataService.updateTransactionEntity(transaction, TransactionStatus.COMPLETED,
                    TransactionSubStatus.SENT_TO_TARGET, txnErrors);
        } else if (httpStatusCode.is4xxClientError()) {
            txnErrors = updateTransactionErrors("Http 400 Error : " + statusCode, ErrorScenarioCode.HTTP_4XX, txnErrors);
            transactionDataService.updateTransactionEntity(transaction, TransactionStatus.OUTBOUND_FAILURE,
                    TransactionSubStatus.OUTBOUND_400_ERROR, txnErrors);
        } else if (httpStatusCode.is5xxServerError()) {
            txnErrors = updateTransactionErrors("Http 500 Error : " + statusCode, ErrorScenarioCode.HTTP_5XX, txnErrors);
            transactionDataService.updateTransactionEntity(transaction, TransactionStatus.OUTBOUND_FAILURE,
                    TransactionSubStatus.OUTBOUND_500_ERROR, txnErrors);
        }
        transactionDataService.auditOutboundTransaction(transaction);
    }

    private static List<TransactionError> updateTransactionErrors(String message, ErrorScenarioCode errorScenarioCode,
                                                                  List<TransactionError> txnErrors) {
        TransactionError transactionError = BusinessUtil.generateTxnError(message,
                errorScenarioCode.getErrorCode(), errorScenarioCode.getErrorMsg(), null);
        txnErrors = BusinessUtil.isCollectionNotNullOrEmpty(txnErrors) ? txnErrors : new ArrayList<>();
        txnErrors.add(transactionError);
        return txnErrors;
    }

    private HttpRequest createRequest(String url, String methodType, String body) {
        return HttpRequest.newBuilder()
                .uri(URI.create(url))
                .version(HttpClient.Version.HTTP_2)
                .header("Content-Type", "application/json")
                .method(methodType, HttpRequest.BodyPublishers.ofString(body))
                .build();
    }
}
