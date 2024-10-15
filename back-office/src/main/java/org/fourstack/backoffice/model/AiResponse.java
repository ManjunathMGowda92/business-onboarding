package org.fourstack.backoffice.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.fourstack.backoffice.enums.EntityStatus;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class AiResponse extends AiRequest{
    private EntityStatus status;
    private String createdTimeStamp;
    private String lastModifiedTimeStamp;
    private List<LinkedOu> linkedOus;
}
