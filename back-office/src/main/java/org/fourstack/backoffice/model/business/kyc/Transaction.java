package org.fourstack.backoffice.model.business.kyc;

import lombok.Data;

@Data
public class Transaction {

    private String id;
    private String ts;
    private String refId;
    private String refUrl;
    private String type;
    private String note;
}
