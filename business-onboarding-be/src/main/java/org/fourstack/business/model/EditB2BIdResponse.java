package org.fourstack.business.model;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
public class EditB2BIdResponse implements Serializable {
    @Serial
    private static final long serialVersionUID = 6164980099147900300L;
    private CommonResponseData commonData;
    private RequesterB2B onboardingB2BIds;
    private RegEditB2BIds regB2BIds;
    private List<EditB2BIdStatus> editStatuses;
    private List<AdditionalInfo> additionalInfoList;
}
