package org.fourstack.business_mock_server.model;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class BankAccount implements Serializable {
    @Serial
    private static final long serialVersionUID = -8262869595212843585L;
    private String businessVPA;
    private String type;
    private String accountNum;
    private String ifsc;
    private String beneficiaryName;
}
