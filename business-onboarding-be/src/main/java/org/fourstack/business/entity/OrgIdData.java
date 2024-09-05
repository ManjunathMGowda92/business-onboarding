package org.fourstack.business.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
public class OrgIdData extends Entity implements Serializable {
    @Serial
    private static final long serialVersionUID = -5074970231461538957L;
    protected String businessKey;
    protected String orgId;
    protected String businessName;
    protected String leiValue;
    protected String leiDocName;
    protected String businessType;
    protected String businessRole;
    protected int currentVersion;
    protected int activeVersion;
    protected String aiId;
    protected String productType;
    protected String primaryIdentifier;
    protected String primaryContactNumber;
    protected String primaryEmail;
}
