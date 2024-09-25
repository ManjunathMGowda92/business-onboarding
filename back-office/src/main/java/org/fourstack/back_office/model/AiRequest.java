package org.fourstack.back_office.model;

import lombok.Data;

@Data
public class AiRequest {
    private String aiId;
    private String name;
    private String description;
    private String subscriberId;
    private String aiWebhookUrl;
    private String headquarter;
}
