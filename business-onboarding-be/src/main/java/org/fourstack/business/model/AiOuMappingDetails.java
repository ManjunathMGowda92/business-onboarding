package org.fourstack.business.model;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class AiOuMappingDetails implements Serializable {
    @Serial
    private static final long serialVersionUID = 5053999864772002824L;

    private String aiId;
    private String ouId;
    private String status;
    private String webhookUrl;
}
