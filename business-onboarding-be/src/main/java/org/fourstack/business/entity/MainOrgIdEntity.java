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
public class MainOrgIdEntity extends OrgIdData implements Serializable {
    @Serial
    private static final long serialVersionUID = 7362338788448255286L;
    private List<EntityVersion> previousVersions;
    private Set<String> publicB2BIds;
    private Set<String> privateB2BIds;
    private Set<String> otherIdentifiers;
    private Set<String> contactNumbers;
    private Set<String> emails;
}
