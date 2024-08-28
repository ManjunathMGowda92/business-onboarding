package org.fourstack.business.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.fourstack.business.model.B2BId;
import org.fourstack.business.model.RequesterB2B;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serial;
import java.io.Serializable;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
@Document(collection = "b2b_identifier")
public class B2BIdentifierEntity extends Entity implements Serializable {

    @Serial
    private static final long serialVersionUID = 7362338788448255286L;
    private String b2bIdValue;
    private String primaryAiId;
    private String primaryOuId;
    private String businessRole;
    private String orgId;
    private Set<String> secondaryAiIds;
    private Set<String> secondaryOuIds;
    private RequesterB2B onboardingB2BId;
    private B2BId b2BId;
}
