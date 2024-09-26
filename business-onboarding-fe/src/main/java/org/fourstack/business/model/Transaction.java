package org.fourstack.business.model;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class Transaction implements Serializable {
    @Serial
    private static final long serialVersionUID = 955442731391258397L;

    private String id;
    private String ts;
    private String refId;
    private String refUrl;
    private String type;
    private String note;
}
