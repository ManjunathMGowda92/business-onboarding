package org.fourstack.business.model;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

import java.util.List;

@Data
public class Validation {
    @JacksonXmlProperty(isAttribute = true)
    private String name;
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<ValidationRule> rule;
}
