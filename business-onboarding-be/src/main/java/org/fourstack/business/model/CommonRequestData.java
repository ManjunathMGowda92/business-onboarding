package org.fourstack.business.model;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class CommonRequestData implements Serializable {
    @Serial
    private static final long serialVersionUID = 8153621759959506433L;
    private Head head;
    private Transaction txn;
    private Device device;
}
