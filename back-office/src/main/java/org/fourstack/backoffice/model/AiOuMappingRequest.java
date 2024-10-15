package org.fourstack.backoffice.model;

import lombok.Data;

@Data
public class AiOuMappingRequest {
    protected String aiId;
    protected String aiName;
    protected String ouId;
    protected String ouName;
    protected String description;
    protected String webhookUrl;
    protected EncryptionDetails encryptionDetails;
}
