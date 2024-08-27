package org.fourstack.business.model;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
public class CheckB2BIds implements Serializable {
    @Serial
    private static final long serialVersionUID = 7680175444935448210L;
    private List<String> ids;
}
