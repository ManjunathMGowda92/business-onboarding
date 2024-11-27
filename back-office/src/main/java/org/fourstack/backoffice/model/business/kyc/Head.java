package org.fourstack.backoffice.model.business.kyc;

import lombok.Data;

@Data
public class Head {

    private String ver;
    private String ts;
    private String msgId;
    private String aiId;
    private String ouId;
    private String orgSysId;
    private String prodType;
}
