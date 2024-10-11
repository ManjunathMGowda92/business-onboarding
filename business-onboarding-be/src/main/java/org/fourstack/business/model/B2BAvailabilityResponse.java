package org.fourstack.business.model;

import lombok.Data;
import org.fourstack.business.enums.BooleanStatus;

import java.io.Serial;
import java.io.Serializable;

@Data
public class B2BAvailabilityResponse implements Serializable {
    @Serial
    private static final long serialVersionUID = 8991948069924004823L;
    private String b2bId;
    private BooleanStatus availability;
}
