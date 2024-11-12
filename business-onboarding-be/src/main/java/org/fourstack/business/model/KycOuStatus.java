package org.fourstack.business.model;

import lombok.Data;
import org.fourstack.business.enums.EntityStatus;
import org.fourstack.business.enums.OperationStatus;

import java.io.Serial;
import java.io.Serializable;

@Data
public class KycOuStatus implements Serializable {
    @Serial
    private static final long serialVersionUID = 4299957736340005664L;
    private int version;
    private EntityStatus versionStatus;
    private OperationStatus kycStatus;
    private String kycTxnId;
    private String mainTxnId;
    private int verificationLevel;
    private String lastModifiedTime;
}
