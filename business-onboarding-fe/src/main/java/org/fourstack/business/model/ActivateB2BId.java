package org.fourstack.business.model;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class ActivateB2BId implements Serializable {
    @Serial
    private static final long serialVersionUID = -8769805208998901268L;
    private String value;
    private String action;
}
