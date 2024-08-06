package org.fourstack.business_mock_server.model;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class CommonResponseData implements Serializable {
    @Serial
    private static final long serialVersionUID = -1038614129050210282L;
    private Head head;
    private Transaction txn;
    private Response response;
}
