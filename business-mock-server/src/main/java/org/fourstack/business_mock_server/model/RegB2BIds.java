package org.fourstack.business_mock_server.model;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
public class RegB2BIds implements Serializable {
    @Serial
    private static final long serialVersionUID = -6221936021909532583L;
    private List<B2BId> ids;
}
