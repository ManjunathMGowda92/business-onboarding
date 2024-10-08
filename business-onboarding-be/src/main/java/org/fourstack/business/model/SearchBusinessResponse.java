package org.fourstack.business.model;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
public class SearchBusinessResponse implements Serializable {
    @Serial
    private static final long serialVersionUID = -4053672861876114090L;
    private CommonResponseData commonData;
    private RequesterB2B onboardingB2BIds;
    private SearchRequest search;
    private SearchResponse searchResult;
    private List<AdditionalInfo> additionalInfoList;
}
