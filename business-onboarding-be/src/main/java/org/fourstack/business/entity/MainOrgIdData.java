package org.fourstack.business.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.fourstack.business.enums.BusinessType;

import java.io.Serial;
import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
public class MainOrgIdData extends Entity implements Serializable {
    @Serial
    private static final long serialVersionUID = -5074970231461538957L;
    protected String businessKey;
    protected String orgId;
    protected String businessName;
    protected String leiValue;
    protected String leiDocName;
    protected String leiType;
    protected BusinessType businessType;
    protected String businessRole;
    protected int currentVersion;
    protected String aiId;
    protected String productType;
}
