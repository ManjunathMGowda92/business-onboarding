package org.fourstack.business.model;

import java.io.Serial;
import java.io.Serializable;

public class SearchCriteria implements Serializable {
    @Serial
    private static final long serialVersionUID = 1441477091938783183L;
    private String searchParameter;
    private String operator;
    private String value;

    public String getSearchParameter() {
        return searchParameter;
    }

    public void setSearchParameter(String searchParameter) {
        this.searchParameter = searchParameter;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
