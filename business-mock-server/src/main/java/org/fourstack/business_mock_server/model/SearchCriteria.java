package org.fourstack.business_mock_server.model;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class SearchCriteria implements Serializable {
    @Serial
    private static final long serialVersionUID = 1441477091938783183L;
    private String searchParameter;
    private String operator;
    private String value;
}
