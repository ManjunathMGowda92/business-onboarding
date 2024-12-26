package org.fourstack.business.entity.business;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.fourstack.business.model.OrgVersions;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serial;
import java.io.Serializable;
import java.util.Map;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
@Document(collection = "organization_details")
public class MainOrgIdEntity extends MainOrgIdData implements Serializable {
    @Serial
    private static final long serialVersionUID = 7362338788448255286L;
    private String defaultB2BId;
    private Set<String> publicB2BIds;
    private Set<String> privateB2BIds;
    private Map<String, OrgVersions> aiStatusMap;
    private String reason;
}
