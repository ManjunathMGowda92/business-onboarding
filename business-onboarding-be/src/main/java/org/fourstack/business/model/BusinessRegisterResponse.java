package org.fourstack.business.model;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
public class BusinessRegisterResponse implements Serializable {
    @Serial
    private static final long serialVersionUID = 8548038039436045652L;
    private CommonResponseData commonData;
    private Institute institute;
    private List<AdditionalInfo> additionalInfoList;
}
