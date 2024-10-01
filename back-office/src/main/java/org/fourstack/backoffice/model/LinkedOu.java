package org.fourstack.backoffice.model;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class LinkedOu implements Serializable {
    @Serial
    private static final long serialVersionUID = -5102117596863240746L;
    private String ouId;
    private String ouName;
}
