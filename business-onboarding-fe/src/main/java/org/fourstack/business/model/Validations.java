package org.fourstack.business.model;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;

import java.util.List;

@Data
@JacksonXmlRootElement(localName = "validations")
public class Validations {
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<Validation> validation;
}
