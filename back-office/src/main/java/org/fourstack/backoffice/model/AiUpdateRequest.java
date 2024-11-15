package org.fourstack.backoffice.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.fourstack.backoffice.enums.EntityStatus;

@EqualsAndHashCode(callSuper = true)
@Data
public class AiUpdateRequest extends AiRequest{
    private EntityStatus status;
    private EncryptionDetails encryptionDetails;
}
