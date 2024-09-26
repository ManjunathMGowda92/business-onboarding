package org.fourstack.business_mock_server.model;

import lombok.Data;
import org.fourstack.business_mock_server.enums.EntityStatus;

import java.io.Serial;
import java.io.Serializable;

@Data
public class ResponseB2BId implements Serializable {
    @Serial
    private static final long serialVersionUID = 6924117629077296967L;
    private String b2bId;
    private String description;
    private BusinessIdentifier businessIdentifier;
    private EntityStatus status;
}
