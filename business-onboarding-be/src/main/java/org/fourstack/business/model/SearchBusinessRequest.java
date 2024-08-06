package org.fourstack.business.model;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
public class SearchBusinessRequest implements Serializable {
    @Serial
    private static final long serialVersionUID = 5825364528864303365L;
    private CommonRequestData commonData;
    private RequesterB2BId onboardingB2BIds;
    private SearchRequest search;
    private List<AdditionalInfo> additionalInfoList;
}
