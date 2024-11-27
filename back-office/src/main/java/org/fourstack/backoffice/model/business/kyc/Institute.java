package org.fourstack.backoffice.model.business.kyc;

import lombok.Data;
import org.fourstack.backoffice.model.Address;

import java.util.List;

@Data
public class Institute {
    private String objectId;
    private String name;
    private String alias;
    private String defaultB2bId;
    private String mccCode;
    private String businessType;
    private String verificationLevel;
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
