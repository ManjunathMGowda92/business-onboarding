package org.fourstack.backoffice.model.business.kyc;

import lombok.Data;

@Data
public class BankAccount {
    private String businessVPA;
    private String type;
    private String accountNum;
    private String ifsc;
    private String beneficiaryName;
}
