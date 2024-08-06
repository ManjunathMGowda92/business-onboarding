package org.fourstack.business.model;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
public class CommonResponseData implements Serializable {
    @Serial
    private static final long serialVersionUID = -1038614129050210282L;
    private Head head;
    private Transaction txn;
    private Response response;
}
