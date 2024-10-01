package org.fourstack.backoffice.model;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class EncryptionDetails implements Serializable {
    @Serial
    private static final long serialVersionUID = -4157085900247658699L;
    private String key;
    private String effectiveFrom;
    private String effectiveTill;
}
