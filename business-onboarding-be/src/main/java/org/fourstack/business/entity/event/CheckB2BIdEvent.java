package org.fourstack.business.entity.event;

import lombok.Data;
import org.fourstack.business.model.CheckB2BIdRequest;
import org.fourstack.business.model.CheckB2BIdResponse;

import java.io.Serial;
import java.io.Serializable;

@Data
public class CheckB2BIdEvent implements Serializable {
    @Serial
    private static final long serialVersionUID = -2531207561632894094L;
    private CheckB2BIdRequest request;
    private CheckB2BIdResponse response;
}
