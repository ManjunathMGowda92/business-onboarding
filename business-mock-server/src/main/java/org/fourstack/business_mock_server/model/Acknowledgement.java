package org.fourstack.business_mock_server.model;

import lombok.Data;
import org.fourstack.business_mock_server.enums.OperationStatus;

import java.io.Serial;
import java.io.Serializable;

@Data
public class Acknowledgement implements Serializable {
    @Serial
    private static final long serialVersionUID = -8712182164837886095L;

    private String apiEndpoint;
    private String msgId;
    private String txnId;
    private OperationStatus result;
    private String errorCode;
    private String errorMsg;
    private String errorField;
    private String timestamp;
}
