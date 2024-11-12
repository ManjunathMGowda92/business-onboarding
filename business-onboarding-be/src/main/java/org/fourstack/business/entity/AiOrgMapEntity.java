package org.fourstack.business.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.fourstack.business.model.BankAccount;
import org.fourstack.business.model.BusinessIdentifier;
import org.fourstack.business.model.ContactNumber;
import org.fourstack.business.model.EntityVersion;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
@Document(collection = "ai_org_mapping_details")
public class AiOrgMapEntity extends MainOrgIdData implements Serializable {
    @Serial
    private static final long serialVersionUID = 9055190319357104868L;
    private int activeVersion;
    private List<EntityVersion> previousVersions;
    private Set<String> publicB2BIds;
    private Set<String> privateB2BIds;
    private int verificationLevel;
    private BusinessIdentifier primaryIdentifier;
    private Set<BusinessIdentifier> otherIdentifiers;
    private ContactNumber primaryContactNumber;
    private Set<ContactNumber> contactNumbers;
    private Set<BankAccount> bankAccounts;
    private String primaryEmail;
    private Set<String> emails;
}
