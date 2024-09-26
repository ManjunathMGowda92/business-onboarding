package org.fourstack.business.model;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
public class SearchRequest implements Serializable {
    @Serial
    private static final long serialVersionUID = -7041967478764312705L;
    private List<SearchCriteria> criteria;
}
