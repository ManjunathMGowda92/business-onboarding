package org.fourstack.business.model;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

@Data
public class ContactNumber implements Serializable {
    @Serial
    private static final long serialVersionUID = -4276807001957570407L;
    private String type;
    private String countryCode;
    private String phoneNumber;

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        ContactNumber that = (ContactNumber) object;
        return Objects.equals(countryCode, that.countryCode) && Objects.equals(phoneNumber, that.phoneNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(countryCode, phoneNumber);
    }
}
