package org.fourstack.backoffice.model.business;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MasterDataRequest implements Serializable {
    @Serial
    private static final long serialVersionUID = -5586821828859024949L;
    private List<AiDetails> aiDetails;
    private List<OuDetails> ouDetails;
    private List<AiOuMappingDetails> aiOuDetails;
}
