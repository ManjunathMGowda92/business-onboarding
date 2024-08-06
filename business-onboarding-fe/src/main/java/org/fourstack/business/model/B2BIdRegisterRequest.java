package org.fourstack.business.model;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
public class B2BIdRegisterRequest implements Serializable {
    @Serial
    private static final long serialVersionUID = 6164980099147900300L;
    private CommonData commonData;
    private RequesterB2BId onboardingB2BIds;
    private RegB2BIds regB2BIds;
    private List<AdditionalInfo> additionalInfoList;
}
