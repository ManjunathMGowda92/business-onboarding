package org.fourstack.backoffice.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.fourstack.backoffice.model.Address;
import org.fourstack.backoffice.model.EncryptionDetails;
import org.fourstack.backoffice.model.LinkedOu;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Document(collection = "agent_institution")
public class AgentInstitutionEntity extends Entity{
    private String id;
    private String name;
    private String alias;
    private String description;
    private String subscriberId;
    private Address registeredAddress;
    private Address communicationAddress;
    private EncryptionDetails encryptionDetails;
    private List<LinkedOu> linkedOus;
}
