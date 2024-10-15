package org.fourstack.backoffice.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.fourstack.backoffice.enums.EntityStatus;

@EqualsAndHashCode(callSuper = true)
@Data
public class OuResponse extends OuRequest{
    private EntityStatus status;
    private String createdTimeStamp;
    private String lastModifiedTimeStamp;
}
