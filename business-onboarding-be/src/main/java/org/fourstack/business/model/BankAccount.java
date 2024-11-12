package org.fourstack.business.model;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

@Data
public class BankAccount implements Serializable {
    @Serial
    private static final long serialVersionUID = -8262869595212843585L;
    private String businessVPA;
    private String type;
    private String accountNum;
    private String ifsc;
    private String beneficiaryName;

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        BankAccount that = (BankAccount) object;
        return Objects.equals(businessVPA, that.businessVPA) && Objects.equals(accountNum, that.accountNum) && Objects.equals(ifsc, that.ifsc);
    }

    @Override
    public int hashCode() {
        return Objects.hash(businessVPA, accountNum, ifsc);
    }
}
