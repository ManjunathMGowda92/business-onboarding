package org.fourstack.backoffice.model.business;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
public class MasterDataResponse implements Serializable {
    @Serial
    private static final long serialVersionUID = -6170500766597354663L;
    private List<AiBackOfficeResponse> aiResponses;
    private List<OuBackOfficeResponse> ouResponses;
    private List<AiOuBackOfficeResponse> aiOuResponses;
}
