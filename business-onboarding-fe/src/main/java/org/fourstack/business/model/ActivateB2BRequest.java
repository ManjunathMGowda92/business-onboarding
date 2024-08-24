package org.fourstack.business.model;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
public class ActivateB2BRequest implements Serializable {
    @Serial
    private static final long serialVersionUID = -1100901176551850378L;
    private CommonData commonData;
    private RequesterB2B onboardingB2BIds;
    private RegActivateB2BId regB2BIds;
    private List<AdditionalInfo> additionalInfoList;
}
