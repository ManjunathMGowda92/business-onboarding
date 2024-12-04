package org.fourstack.backoffice.model.business.kyc;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class EditInstitute extends CommonInstitute{
    private EditBusinessIdentifier primaryIdentifier;
    private List<EditBusinessIdentifier> otherIdentifiers;
}
