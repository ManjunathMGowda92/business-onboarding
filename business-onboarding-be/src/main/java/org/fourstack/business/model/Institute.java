package org.fourstack.business.model;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
public class Institute implements Serializable {
    @Serial
    private static final long serialVersionUID = 2252663400710646311L;
    private String objectId;
    private String name;
    private String alias;
    private String defaultB2bId;
    private String mccCode;
    private Lei lei;
    private BusinessIdentifier primaryIdentifier;
    private List<BusinessIdentifier> otherIdentifiers;
    private List<Address> addresses;
    private List<BankAccount> bankAccounts;
    private List<ContactNumber> contactNumbers;
    private ContactNumber primaryContact;
    private List<String> emails;
    private String primaryEmail;
}
