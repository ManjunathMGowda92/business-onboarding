package org.fourstack.backoffice.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.fourstack.backoffice.enums.EntityStatus;

@EqualsAndHashCode(callSuper = true)
@Data
public class AiOuMappingResponse extends AiOuMappingRequest{
    private EntityStatus status;
    private String createdTimeStamp;
    private String lastModifiedTimeStamp;
    private BackOfficeAck ack;
}
