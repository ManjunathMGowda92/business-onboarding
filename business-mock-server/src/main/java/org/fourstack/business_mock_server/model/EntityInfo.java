package org.fourstack.business_mock_server.model;

import lombok.Data;
import org.fourstack.business_mock_server.enums.EntityStatus;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
public class EntityInfo implements Serializable {
    @Serial
    private static final long serialVersionUID = 4430422631868765294L;

    private String businessName;
    private String mccCode;
    private Address address;
    private EntityStatus status;
    private List<ResponseB2BId> b2BIdInfoList;
}
