package org.fourstack.business_mock_server.model;

import lombok.Data;
import org.fourstack.business_mock_server.enums.BooleanStatus;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
public class InstituteInfo implements Serializable {
    @Serial
    private static final long serialVersionUID = -789502793382783420L;
    private BooleanStatus isBusinessExist;
    private BooleanStatus isMultipleOrgAllowed;
    private List<BusinessDetails> businessDetails;
}
