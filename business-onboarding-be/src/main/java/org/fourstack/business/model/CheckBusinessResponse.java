package org.fourstack.business.model;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
public class CheckBusinessResponse implements Serializable {
    @Serial
    private static final long serialVersionUID = -7917341591213746917L;
    private CommonResponseData commonData;
    private CheckInstitute checkInstitute;
    private CheckInstituteResponse instituteStatus;
    private List<AdditionalInfo> additionalInfoList;
}
