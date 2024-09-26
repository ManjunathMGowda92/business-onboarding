package org.fourstack.backoffice.model;

import lombok.Data;

@Data
public class OuRequest {
    private String ouId;
    private String name;
    private String description;
    private String ifscCode;
    private String businessVPA;
    private String branchLocation;
}
