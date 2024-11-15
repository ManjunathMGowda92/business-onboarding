package org.fourstack.business.model.backoffice.masterdata;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
public class MasterDataRequest implements Serializable {
    @Serial
    private static final long serialVersionUID = -5586821828859024949L;
    private List<AiDetails> aiDetails;
    private List<OuDetails> ouDetails;
    private List<AiOuMappingDetails> aiOuDetails;
}
