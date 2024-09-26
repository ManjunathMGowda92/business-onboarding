package org.fourstack.business.entity;

import lombok.Data;
import org.fourstack.business.enums.EntityStatus;
import org.springframework.data.annotation.Id;

import java.io.Serial;
import java.io.Serializable;

@Data
public class Entity implements Serializable {
    @Serial
    private static final long serialVersionUID = -6279412791580405690L;
    @Id
    protected String key;
    protected EntityStatus status;
    protected String createdTimeStamp;
    protected String lastModifiedTimeStamp;
}
