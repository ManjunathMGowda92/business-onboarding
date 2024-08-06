package org.fourstack.business.mapper;

import org.fourstack.business.entity.event.Message;
import org.fourstack.business.enums.EventType;
import org.fourstack.business.enums.OperationStatus;
import org.fourstack.business.model.Acknowledgement;
import org.fourstack.business.utils.BusinessUtil;
import org.springframework.stereotype.Component;

@Component
public class ResponseMapper {

    public Acknowledgement getSuccessAck(String msgId, String txnId, String apiEndpoint) {
        return getAcknowledgement(OperationStatus.SUCCESS, msgId, txnId, apiEndpoint);
    }

    private Acknowledgement getAcknowledgement(OperationStatus status, String msgId, String txnId, String apiEndpoint) {
        Acknowledgement ack = new Acknowledgement();
        ack.setResult(status);
        ack.setTimestamp(BusinessUtil.getCurrentTimeStamp());
        ack.setMsgId(msgId);
        ack.setTxnId(txnId);
        ack.setApiEndpoint(apiEndpoint);
        return ack;
    }

    public Acknowledgement getFailureAck(String msgId, String txnId, String apiEndpoint,
                                         String errorCode, String errorMsg, String fieldName) {
        Acknowledgement ack = getAcknowledgement(OperationStatus.FAILURE, msgId, txnId, apiEndpoint);
        ack.setErrorCode(errorCode);
        ack.setErrorMsg(errorMsg);
        ack.setErrorField(fieldName);
        return ack;
    }

    public <R> Message<R, Acknowledgement> constructMessage(R request, EventType eventType, String msgId,
                                                            String txnId, String apiEndpoint) {
        Message<R, Acknowledgement> message = new Message<>();
        message.setRequest(request);
        Acknowledgement ack = getSuccessAck(msgId, txnId, apiEndpoint);
        message.setAck(ack);
        message.setRequestObjType(request.getClass());
        message.setAckObjType(ack.getClass());
        message.setEventType(eventType);
        //message.setIdentifierKey(eventType.name());
        message.setIdentifierKey(msgId);
        return message;
    }
}
