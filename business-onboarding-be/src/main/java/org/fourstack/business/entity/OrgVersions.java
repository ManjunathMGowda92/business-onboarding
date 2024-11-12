package org.fourstack.business.entity;

import lombok.Data;
import org.fourstack.business.enums.EntityStatus;
import org.fourstack.business.model.EntityVersion;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
public class OrgVersions implements Serializable {
    @Serial
    private static final long serialVersionUID = 5910709590163191087L;
    private EntityStatus aiOrgStatus;
    private List<EntityVersion> previousVersions;
}
