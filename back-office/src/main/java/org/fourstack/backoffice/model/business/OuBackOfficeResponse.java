package org.fourstack.backoffice.model.business;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class OuBackOfficeResponse extends OuDetails {
    private Response response;
    protected String createdTimeStamp;
    protected String lastModifiedTimeStamp;
}
