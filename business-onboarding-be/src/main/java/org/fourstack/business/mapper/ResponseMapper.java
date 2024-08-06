package org.fourstack.business.mapper;

import org.fourstack.business.enums.OperationStatus;
import org.fourstack.business.model.Acknowledgement;
import org.fourstack.business.model.B2BIdRegisterRequest;
import org.fourstack.business.model.B2BIdRegisterResponse;
import org.fourstack.business.model.BusinessRegisterRequest;
import org.fourstack.business.model.BusinessRegisterResponse;
import org.fourstack.business.model.CommonRequestData;
import org.fourstack.business.model.CommonResponseData;
import org.fourstack.business.model.Head;
import org.fourstack.business.model.Response;
import org.fourstack.business.utils.BusinessUtil;
import org.springframework.stereotype.Component;

@Component
public class ResponseMapper {

    public Acknowledgement getSuccessAck(String msgId, String txnId, String apiEndpoint) {
        Acknowledgement ack = new Acknowledgement();
        ack.setResult(OperationStatus.SUCCESS);
        ack.setTimestamp(BusinessUtil.getCurrentTimeStamp());
        ack.setMsgId(msgId);
        ack.setTxnId(txnId);
        ack.setApiEndpoint(apiEndpoint);
        return ack;
    }

    public BusinessRegisterResponse generateSuccessBusinessResponse(BusinessRegisterRequest request) {
        BusinessRegisterResponse businessResponse = generateBusinessRegisterResponse(request);
        CommonResponseData commonData = businessResponse.getCommonData();
        Head head = commonData.getHead();
        Response build = generateSuccessResponse(head.getMsgId());
        head.setMsgId(BusinessUtil.generateAlphaNumericID(32, "MSG"));
        commonData.setResponse(build);
        businessResponse.setCommonData(commonData);
        return businessResponse;
    }

    public BusinessRegisterResponse generateFailureBusinessResponse(BusinessRegisterRequest request, String errorCode,
                                                                    String errorMsg, String errorField) {
        BusinessRegisterResponse businessResponse = generateBusinessRegisterResponse(request);
        CommonResponseData commonData = businessResponse.getCommonData();
        Head head = commonData.getHead();
        Response build = generateFailureResponse(head.getMsgId(), errorCode, errorMsg, errorField);
        head.setMsgId(BusinessUtil.generateAlphaNumericID(32, "MSG"));
        commonData.setResponse(build);
        businessResponse.setCommonData(commonData);
        return businessResponse;
    }

    public B2BIdRegisterResponse generateSuccessB2BResponse(B2BIdRegisterRequest request) {
        B2BIdRegisterResponse response = generateB2BRegisterResponse(request);
        CommonResponseData commonData = response.getCommonData();
        Head head = commonData.getHead();
        Response build = generateSuccessResponse(head.getMsgId());
        head.setMsgId(BusinessUtil.generateAlphaNumericID(32, "MSG"));
        commonData.setResponse(build);
        response.setCommonData(commonData);
        return response;
    }

    public B2BIdRegisterResponse generateFailureB2BResponse(B2BIdRegisterRequest request, String errorCode,
                                                            String errorMsg, String errorField) {
        B2BIdRegisterResponse response = generateB2BRegisterResponse(request);
        CommonResponseData commonData = response.getCommonData();
        Head head = commonData.getHead();
        Response build = generateFailureResponse(head.getMsgId(), errorCode, errorMsg, errorField);
        head.setMsgId(BusinessUtil.generateAlphaNumericID(32, "MSG"));
        commonData.setResponse(build);
        return response;
    }

    private B2BIdRegisterResponse generateB2BRegisterResponse(B2BIdRegisterRequest request) {
        B2BIdRegisterResponse response = new B2BIdRegisterResponse();
        CommonResponseData responseData = constructCommonDataForResponse(request.getCommonData());
        response.setCommonData(responseData);
        response.setAdditionalInfoList(request.getAdditionalInfoList());
        response.setRegB2BIds(request.getRegB2BIds());
        response.setOnboardingB2BIds(request.getOnboardingB2BIds());
        return response;
    }

    private Response generateSuccessResponse(String msgId) {
        return Response.builder().requestMsgId(msgId)
                .result(OperationStatus.SUCCESS)
                .build();
    }

    private Response generateFailureResponse(String msgId, String errorCode, String errMsg, String errField) {
        return Response.builder().requestMsgId(msgId)
                .result(OperationStatus.FAILURE)
                .errorCode(errorCode)
                .errorMsg(errMsg)
                .errorField(errField)
                .build();
    }

    public BusinessRegisterResponse generateBusinessRegisterResponse(BusinessRegisterRequest request) {
        BusinessRegisterResponse response = new BusinessRegisterResponse();
        CommonResponseData responseData = constructCommonDataForResponse(request.getCommonData());
        response.setCommonData(responseData);
        response.setInstitute(request.getInstitute());
        response.setAdditionalInfoList(request.getAdditionalInfoList());
        return response;
    }

    private CommonResponseData constructCommonDataForResponse(CommonRequestData commonData) {
        CommonResponseData responseData = new CommonResponseData();
        responseData.setHead(commonData.getHead());
        responseData.setTxn(commonData.getTxn());
        return responseData;
    }
}
