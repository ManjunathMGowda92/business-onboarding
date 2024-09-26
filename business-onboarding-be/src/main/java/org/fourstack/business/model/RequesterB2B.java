package org.fourstack.business.model;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class RequesterB2B implements Serializable {
    @Serial
    private static final long serialVersionUID = -6597535379731769020L;
    private String requesterB2BId;
}
