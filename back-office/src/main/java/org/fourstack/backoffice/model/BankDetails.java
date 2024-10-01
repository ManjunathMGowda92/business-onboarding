package org.fourstack.backoffice.model;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class BankDetails implements Serializable {
    @Serial
    private static final long serialVersionUID = -1890409060604876266L;
    private String name;
    private String ifscCode;
    private Address branchAddress;
}
