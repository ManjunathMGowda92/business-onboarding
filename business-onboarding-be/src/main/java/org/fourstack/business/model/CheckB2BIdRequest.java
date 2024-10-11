package org.fourstack.business.model;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
public class CheckB2BIdRequest implements Serializable {
    @Serial
    private static final long serialVersionUID = 6164980099147900300L;
    private CommonRequestData commonData;
    private CheckB2BIds checkB2BIds;
    private List<AdditionalInfo> additionalInfoList;
}
