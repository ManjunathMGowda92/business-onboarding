package org.fourstack.business.model.backoffice;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class OuDetails implements Serializable {
    @Serial
    private static final long serialVersionUID = -6904241064985629378L;
    private String ouId;
    private String name;
    private String status;
}
