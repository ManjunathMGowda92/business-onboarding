package org.fourstack.business.model;

import lombok.Data;
import org.fourstack.business.enums.OperationStatus;

import java.io.Serial;
import java.io.Serializable;

@Data
public class KycStatusResponse implements Serializable {
    @Serial
    private static final long serialVersionUID = 9022812436565424075L;
    private String ouId;
    private OperationStatus result;
    private String reason;
    private String responseReceivedTime;
}
