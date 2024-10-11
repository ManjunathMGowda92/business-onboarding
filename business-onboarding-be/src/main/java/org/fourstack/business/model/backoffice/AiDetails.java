package org.fourstack.business.model.backoffice;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class AiDetails implements Serializable {
    @Serial
    private static final long serialVersionUID = 1157812260310622784L;
    private String aiId;
    private String name;
    private String subscriberId;
    private String status;
    private String type;
}
