package org.fourstack.business.model.backoffice;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class EncryptionDetails implements Serializable {
    @Serial
    private static final long serialVersionUID = 8674994877687689140L;
    private String key;
    private String effectiveStartDate;
    private String effectiveEndDate;
}
