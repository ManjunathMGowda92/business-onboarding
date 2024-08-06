package org.fourstack.business.model;

import lombok.Data;
import org.fourstack.business.enums.EntityStatus;

import java.io.Serial;
import java.io.Serializable;

@Data
public class EntityVersion implements Serializable {
    @Serial
    private static final long serialVersionUID = -8383948772174304053L;
    private String txnId;
    private int version;
    private EntityStatus status;
}
