package org.fourstack.business.model;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class Device implements Serializable {
    @Serial
    private static final long serialVersionUID = -8631514088008747358L;
    private Tag tag;
}
