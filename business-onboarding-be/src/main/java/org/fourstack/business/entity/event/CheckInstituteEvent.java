package org.fourstack.business.entity.event;

import lombok.Data;
import org.fourstack.business.model.CheckBusinessRequest;
import org.fourstack.business.model.CheckBusinessResponse;

import java.io.Serial;
import java.io.Serializable;

@Data
public class CheckInstituteEvent implements Serializable {
    @Serial
    private static final long serialVersionUID = -7659985373865919291L;
    private String identifier;
    private CheckBusinessRequest request;
    private CheckBusinessResponse response;
}
