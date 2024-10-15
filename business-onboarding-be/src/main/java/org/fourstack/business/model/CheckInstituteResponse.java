package org.fourstack.business.model;

import lombok.Data;
import org.fourstack.business.enums.BooleanStatus;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
public class CheckInstituteResponse implements Serializable {
    @Serial
    private static final long serialVersionUID = 5902701386971970469L;
    private BooleanStatus isBusinessExist;
    private BooleanStatus isMultipleOrgAllowed;
    private List<BusinessDetails> instituteDetails;
}
