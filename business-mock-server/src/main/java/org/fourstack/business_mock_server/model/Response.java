package org.fourstack.business_mock_server.model;

import lombok.Builder;
import lombok.Data;
import org.fourstack.business_mock_server.enums.OperationStatus;

import java.io.Serial;
import java.io.Serializable;

@Data
@Builder
public class Response implements Serializable {
    @Serial
    private static final long serialVersionUID = 7018982767846649843L;
    private String requestMsgId;
    private OperationStatus result;
    private String errorCode;
    private String errorMsg;
    private String errorField;
}
