package org.fourstack.business.model.backoffice.kyc;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.fourstack.business.enums.EventType;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BackOfficeKycRequest {
    private EventType eventType;
    private String key;
    private Object request;
}
