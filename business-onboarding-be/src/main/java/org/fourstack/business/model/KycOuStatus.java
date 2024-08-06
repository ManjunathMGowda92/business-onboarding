package org.fourstack.business.model;

import lombok.Data;
import org.fourstack.business.enums.OperationStatus;

import java.io.Serial;
import java.io.Serializable;

@Data
public class KycOuStatus implements Serializable {
    @Serial
    private static final long serialVersionUID = 4299957736340005664L;
    private String ouId;
    private OperationStatus status;
}
