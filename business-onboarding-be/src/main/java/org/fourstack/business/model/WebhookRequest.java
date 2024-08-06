package org.fourstack.business.model;

import lombok.Data;
import org.springframework.http.HttpMethod;

import java.io.Serial;
import java.io.Serializable;

@Data
public class WebhookRequest implements Serializable {
    @Serial
    private static final long serialVersionUID = 8905496884300035034L;

    private String url;
    private HttpMethod methodType;
    private String requestBody;
}
