package org.fourstack.business.model;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class Head implements Serializable {
    @Serial
    private static final long serialVersionUID = -9030299114457714826L;

    private String ver;
    private String ts;
    private String msgId;
    private String aiId;
    private String ouId;
    private String orgSysId;
    private String prodType;
}
