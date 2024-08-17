package org.fourstack.business.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
public class EditB2BId extends B2BId implements Serializable {
    @Serial
    private static final long serialVersionUID = 6015701694404746589L;
    private String action;
    private String primaryAi;
}
