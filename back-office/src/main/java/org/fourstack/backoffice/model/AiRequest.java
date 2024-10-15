package org.fourstack.backoffice.model;

import lombok.Data;

@Data
public class AiRequest {
    protected String agentInstitutionId;
    protected String agentInstitutionName;
    protected String agentInstitutionAliasName;
    protected String description;
    protected String subscriberId;
    protected String type;
    protected Address registeredAddress;
    protected Address communicationAddress;
}
