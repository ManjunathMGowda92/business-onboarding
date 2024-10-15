package org.fourstack.business.model.backoffice;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.fourstack.business.model.Response;

@EqualsAndHashCode(callSuper = true)
@Data
public class OuBackOfficeResponse extends OuDetails{
    private Response response;
    protected String createdTimeStamp;
    protected String lastModifiedTimeStamp;
}
