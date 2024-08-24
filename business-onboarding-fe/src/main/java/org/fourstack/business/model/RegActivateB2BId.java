package org.fourstack.business.model;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
public class RegActivateB2BId implements Serializable {
    @Serial
    private static final long serialVersionUID = -2738739585236718314L;
    private List<ActivateB2BId> b2BIds;
}
