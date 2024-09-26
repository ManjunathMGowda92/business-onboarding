package org.fourstack.business.entity.event;

import lombok.Data;
import org.fourstack.business.model.BusinessRegisterRequest;
import org.fourstack.business.model.BusinessRegisterResponse;

import java.io.Serial;
import java.io.Serializable;

@Data
public class BusinessEvent implements Serializable {
    @Serial
    private static final long serialVersionUID = -6852944146820962L;
    private BusinessRegisterRequest request;
    private BusinessRegisterResponse response;
}
