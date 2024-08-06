package org.fourstack.business.model;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class TransactionError implements Serializable {
    @Serial
    private static final long serialVersionUID = -2429312020492990008L;
    private String message;
    private String errorCode;
    private String errorMsg;
    private String errorField;
    private String timeStamp;
}
