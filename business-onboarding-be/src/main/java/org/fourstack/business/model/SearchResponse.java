package org.fourstack.business.model;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
public class SearchResponse implements Serializable {
    @Serial
    private static final long serialVersionUID = 3304918305962789602L;
    private List<EntityInfo> entityInfoList;
}
