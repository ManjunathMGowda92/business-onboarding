package org.fourstack.business.entity.event;

import lombok.Data;
import org.fourstack.business.model.B2BIdRegisterRequest;
import org.fourstack.business.model.B2BIdRegisterResponse;

import java.io.Serial;
import java.io.Serializable;

@Data
public class B2BIdRegisterEvent implements Serializable {
    @Serial
    private static final long serialVersionUID = -1739219946399300952L;
    private String identifier;
    private B2BIdRegisterRequest request;
    private B2BIdRegisterResponse response;
}
