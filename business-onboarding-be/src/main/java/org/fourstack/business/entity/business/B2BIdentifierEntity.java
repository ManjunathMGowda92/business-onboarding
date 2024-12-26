package org.fourstack.business.entity.business;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.fourstack.business.entity.Entity;
import org.fourstack.business.enums.EntityStatus;
import org.fourstack.business.model.B2BIdDetails;
import org.fourstack.business.model.BankAccount;
import org.fourstack.business.model.RequesterB2B;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serial;
import java.io.Serializable;
import java.util.Map;

@EqualsAndHashCode(callSuper = true)
@Data
@Document(collection = "b2b_identifier")
public class B2BIdentifierEntity extends Entity implements Serializable {

    @Serial
    private static final long serialVersionUID = 7362338788448255286L;
    private String b2bIdValue;
    private String primaryAiId;
    private String businessRole;
    private String orgId;
    private Map<String, Integer> secondaryAiMap;
    private int ordinalValue;
    private RequesterB2B onboardingB2BId;
    private B2BIdDetails b2BId;
    private BankAccount bankAccount;
    private Map<String, EntityStatus> aiStatusMap;
}
