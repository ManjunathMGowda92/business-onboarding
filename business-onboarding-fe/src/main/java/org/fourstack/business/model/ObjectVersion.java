package org.fourstack.business.model;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class ObjectVersion implements Serializable {
    @Serial
    private static final long serialVersionUID = -1939282540319297935L;
    private int currentVersion;
    private int newVersion;
}
