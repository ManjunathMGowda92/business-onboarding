package org.fourstack.backoffice.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class AiOuEncryptionDetailsRequest extends EncryptionDetails{
    private String aiId;
    private String ouId;
}
