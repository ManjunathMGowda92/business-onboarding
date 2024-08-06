package org.fourstack.business.model;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;


@Data
public class BusinessRegisterRequest implements Serializable {
    @Serial
    private static final long serialVersionUID = 2232682083146648976L;
    private CommonData commonData;
    private Institute institute;
    private List<AdditionalInfo> additionalInfoList;
}
