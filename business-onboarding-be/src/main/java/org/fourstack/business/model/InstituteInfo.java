package org.fourstack.business.model;

import lombok.Data;
import org.fourstack.business.enums.BooleanStatus;

import java.io.Serial;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

@Data
public class InstituteInfo implements Serializable {
    @Serial
    private static final long serialVersionUID = -789502793382783420L;
    private BooleanStatus isBusinessExist;
    private BooleanStatus isMultipleOrgAllowed;
    private String businessName;
    private Collection<String> b2bIds;
}
