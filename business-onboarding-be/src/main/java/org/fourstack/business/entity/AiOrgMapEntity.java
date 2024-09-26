package org.fourstack.business.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.fourstack.business.enums.EntityStatus;
import org.fourstack.business.model.EntityVersion;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
@Document(collection = "ai_org_mapping_details")
public class AiOrgMapEntity extends OrgIdData implements Serializable {
    @Serial
    private static final long serialVersionUID = 9055190319357104868L;
    private Map<String, EntityStatus> aiStatusMap;
    private List<EntityVersion> previousVersions;
    private Set<String> publicB2BIds;
    private Set<String> privateB2BIds;
    private Set<String> otherIdentifiers;
    private String primaryContactNumber;
    private Set<String> contactNumbers;
    private String primaryEmail;
    private Set<String> emails;
}
