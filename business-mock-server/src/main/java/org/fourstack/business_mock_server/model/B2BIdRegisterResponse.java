package org.fourstack.business_mock_server.model;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
public class B2BIdRegisterResponse implements Serializable {
    @Serial
    private static final long serialVersionUID = 380561741596560283L;
    private CommonResponseData commonData;
    private RequesterB2B onboardingB2BIds;
    private RegB2BIds regB2BIds;
    private List<AdditionalInfo> additionalInfoList;
}
