package org.fourstack.business.model;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

@Data
public class ValidationRule {
    @JacksonXmlProperty(isAttribute = true)
    private String identifier;
    @JacksonXmlProperty(isAttribute = true)
    private String fieldName;
    @JacksonXmlProperty(isAttribute = true)
    private String expression;
    @JacksonXmlProperty(isAttribute = true)
    private String errorCode;
    @JacksonXmlProperty(isAttribute = true)
    private String errorMessage;
    @JacksonXmlProperty(isAttribute = true)
    private String isOptional;
}
