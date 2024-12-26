package org.fourstack.backoffice.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.fourstack.backoffice.model.Address;
import org.fourstack.backoffice.model.BankDetails;
import org.springframework.data.mongodb.core.mapping.Document;

@EqualsAndHashCode(callSuper = true)
@Data
@Document(collection = "operation_unit")
public class OperationUnit extends Entity{
    private String id;
    private String name;
    private String alias;
    private String description;
    private String mailId;
    private Address registeredAddress;
    private Address communicationAddress;
    private BankDetails bankDetails;
}
