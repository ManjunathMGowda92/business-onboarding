package org.fourstack.business_mock_server.model;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class RequesterB2BId implements Serializable {
    @Serial
    private static final long serialVersionUID = -6597535379731769020L;
    private String requestorB2BId;
}
