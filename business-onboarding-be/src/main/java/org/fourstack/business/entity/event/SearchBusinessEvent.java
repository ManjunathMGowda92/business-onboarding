package org.fourstack.business.entity.event;

import lombok.Data;
import org.fourstack.business.model.SearchBusinessRequest;
import org.fourstack.business.model.SearchBusinessResponse;

import java.io.Serial;
import java.io.Serializable;

@Data
public class SearchBusinessEvent implements Serializable {
    @Serial
    private static final long serialVersionUID = 7314761980308894885L;
    private SearchBusinessRequest request;
    private SearchBusinessResponse response;
}
