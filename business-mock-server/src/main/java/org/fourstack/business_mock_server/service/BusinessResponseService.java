package org.fourstack.business_mock_server.service;

import org.fourstack.business_mock_server.enums.OperationStatus;
import org.fourstack.business_mock_server.exceptions.InvalidInputException;
import org.fourstack.business_mock_server.model.Acknowledgement;
import org.fourstack.business_mock_server.model.B2BIdRegisterResponse;
import org.fourstack.business_mock_server.model.BusinessRegisterResponse;
import org.fourstack.business_mock_server.model.CheckBusinessResponse;
import org.fourstack.business_mock_server.model.CommonResponseData;
import org.fourstack.business_mock_server.model.SearchBusinessResponse;
import org.fourstack.business_mock_server.util.MockServerUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class BusinessResponseService {
    private static final Logger logger = LoggerFactory.getLogger(BusinessResponseService.class);

    public Acknowledgement processResponseData(Object response, String endpoint) {
        String data = MockServerUtil.convertToString(response);
        logger.info("{} - Request received : {}", this.getClass().getSimpleName(), data);
        return switch (response) {
            case BusinessRegisterResponse businessResponse ->
                    generateAcknowledgement(endpoint, businessResponse.getCommonData());
            case B2BIdRegisterResponse businessResponse ->
                    generateAcknowledgement(endpoint, businessResponse.getCommonData());
            case CheckBusinessResponse businessResponse ->
                    generateAcknowledgement(endpoint, businessResponse.getCommonData());
            case SearchBusinessResponse searchResponse ->
                    generateAcknowledgement(endpoint, searchResponse.getCommonData());
            default -> {
                logger.error("Invalid Response received");
                throw new InvalidInputException("Invalid Response received");
            }
        };
    }

    private Acknowledgement generateAcknowledgement(String endpoint, CommonResponseData responseData) {
        Acknowledgement ack = new Acknowledgement();
        ack.setApiEndpoint(endpoint);
        ack.setResult(OperationStatus.SUCCESS);
        ack.setMsgId(responseData.getHead().getMsgId());
        ack.setTxnId(responseData.getTxn().getId());
        ack.setTimestamp(getCurrentTimeStamp());
        return ack;
    }

    private String getCurrentTimeStamp() {
        OffsetDateTime dateTime = OffsetDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
        return dateTime.format(formatter);
    }
}
