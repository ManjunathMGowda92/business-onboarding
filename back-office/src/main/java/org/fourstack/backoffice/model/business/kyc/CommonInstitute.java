package org.fourstack.backoffice.model.business.kyc;

import lombok.Data;
import org.fourstack.backoffice.model.Address;

import java.util.List;

@Data
public class CommonInstitute {
    protected String objectId;
    protected String name;
    protected String alias;
    protected String defaultB2bId;
    protected String mccCode;
    protected String businessType;
    protected String verificationLevel;
    protected Lei lei;
    protected List<Address> addresses;
    protected List<BankAccount> bankAccounts;
    protected List<ContactNumber> contactNumbers;
    protected ContactNumber primaryContact;
    protected List<String> emails;
    protected String primaryEmail;
}
