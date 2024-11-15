package org.fourstack.business.model.backoffice.kyc;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.fourstack.business.model.AdditionalInfo;
import org.fourstack.business.model.Head;
import org.fourstack.business.model.Institute;
import org.fourstack.business.model.Transaction;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KycCreateBusinessRequest {
    private String requestType;
    private Head head;
    private Transaction txn;
    private Institute institute;
    private List<AdditionalInfo> additionalInfos;
}
