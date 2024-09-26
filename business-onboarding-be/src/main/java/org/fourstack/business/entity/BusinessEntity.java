package org.fourstack.business.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.fourstack.business.model.AdditionalInfo;
import org.fourstack.business.model.Device;
import org.fourstack.business.model.Head;
import org.fourstack.business.model.Institute;
import org.fourstack.business.model.Transaction;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Document(collection = "business")
public class BusinessEntity extends Entity implements Serializable {
    @Serial
    private static final long serialVersionUID = 7362338788448255286L;
    private String businessRole;
    private Head head;
    private Transaction txn;
    private Device device;
    private Institute institute;
    private List<AdditionalInfo> additionalInfoList;
}
