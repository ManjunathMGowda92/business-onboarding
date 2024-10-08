package org.fourstack.backoffice.model;

import lombok.Data;

@Data
public class AiOuMappingRequest {
    private String aiId;
    private String aiName;
    private String ouId;
    private String ouName;
    private String description;
}
