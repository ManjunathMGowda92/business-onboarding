package org.fourstack.business.model;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
public class CheckBusinessRequest implements Serializable {
    @Serial
    private static final long serialVersionUID = -8974870624020770782L;
    private CommonData commonData;
        private CheckInstitute checkInstitute;
    private List<AdditionalInfo> additionalInfoList;
}
