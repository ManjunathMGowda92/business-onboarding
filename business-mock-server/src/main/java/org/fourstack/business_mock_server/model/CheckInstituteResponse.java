package org.fourstack.business_mock_server.model;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class CheckInstituteResponse implements Serializable {
    @Serial
    private static final long serialVersionUID = 5902701386971970469L;
    private InstituteInfo instituteInfo;
}
