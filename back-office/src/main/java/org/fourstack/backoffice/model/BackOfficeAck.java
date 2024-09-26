package org.fourstack.backoffice.model;

import lombok.Data;
import org.fourstack.backoffice.enums.OperationStatus;

@Data
public class BackOfficeAck {
    private OperationStatus status;
    private String errorCode;
    private String errorMsg;
    private String errorField;
}
