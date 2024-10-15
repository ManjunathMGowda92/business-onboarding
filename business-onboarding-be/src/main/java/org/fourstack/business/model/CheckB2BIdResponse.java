package org.fourstack.business.model;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
public class CheckB2BIdResponse implements Serializable {
    @Serial
    private static final long serialVersionUID = 4980831265343779784L;
    private CommonResponseData commonData;
    private CheckB2BIds checkB2BIds;
    private List<B2BAvailabilityResponse> b2bIdStatuses;
    private List<AdditionalInfo> additionalInfoList;
}
