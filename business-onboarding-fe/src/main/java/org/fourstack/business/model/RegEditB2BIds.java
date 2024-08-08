package org.fourstack.business.model;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
public class RegEditB2BIds implements Serializable {
    @Serial
    private static final long serialVersionUID = -6221936021909532583L;
    private List<EditB2BId> editIds;
}
