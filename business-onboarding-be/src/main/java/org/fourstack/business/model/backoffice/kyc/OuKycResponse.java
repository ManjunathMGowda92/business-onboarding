package org.fourstack.business.model.backoffice.kyc;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.fourstack.business.enums.OperationStatus;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OuKycResponse {
    private String ouId;
    private OperationStatus result;
    private String reason;
    private String errorCode;
    private String errorField;
}
