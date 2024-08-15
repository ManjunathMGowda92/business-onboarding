package org.fourstack.business.model;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Collection;

@Data
public class BusinessDetails implements Serializable {
    @Serial
    private static final long serialVersionUID = -789502793382783420L;
    private String businessName;
    private Collection<String> b2bIds;
}
