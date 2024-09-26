package org.fourstack.backoffice.model;

import lombok.Data;

@Data
public class OuRequest {
    protected String ouId;
    protected String name;
    protected String description;
    protected String ifscCode;
    protected String businessVPA;
    protected String branchLocation;
}
