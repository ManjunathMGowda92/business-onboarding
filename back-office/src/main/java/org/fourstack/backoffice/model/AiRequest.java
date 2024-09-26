package org.fourstack.backoffice.model;

import lombok.Data;

@Data
public class AiRequest {
    protected String aiId;
    protected String name;
    protected String description;
    protected String subscriberId;
    protected String aiWebhookUrl;
    protected String headquarter;
}
