package org.fourstack.business.model;

import lombok.Data;
import org.fourstack.business.enums.OperationStatus;

import java.io.Serial;
import java.io.Serializable;

@Data
public class EditB2BIdStatus implements Serializable {
    @Serial
    private static final long serialVersionUID = 6164980099147900300L;
    private String b2bId;
    private OperationStatus updateStatus;
}
