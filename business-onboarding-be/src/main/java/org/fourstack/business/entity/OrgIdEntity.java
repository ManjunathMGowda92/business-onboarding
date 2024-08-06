package org.fourstack.business.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.fourstack.business.model.EntityVersion;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
@Document(collection = "organization_details")
public class OrgIdEntity extends Entity implements Serializable {
    @Serial
    private static final long serialVersionUID = 7362338788448255286L;
    private String businessKey;
    private String orgId;
    private String businessName;
    private String leiValue;
    private String leiDocName;
    private String businessType;
    private String businessRole;
    private int currentVersion;
    private int activeVersion;
    private List<EntityVersion> previousVersions;
    private String defaultB2BId;
    private Set<String> publicB2BIds;
    private Set<String> privateB2BIds;
    private String aiId;
    private String ouId;
    private String productType;
    private String primaryIdentifier;
    private Set<String> otherIdentifiers;
    private String primaryContactNumber;
    private Set<String> contactNumbers;
    private String primaryEmail;
    private Set<String> emails;
}
