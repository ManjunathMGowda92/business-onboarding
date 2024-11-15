package org.fourstack.backoffice.model.business;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class AiOuBackOfficeResponse extends AiOuMappingDetails {
    private Response response;
    protected String createdTimeStamp;
    protected String lastModifiedTimeStamp;
}
