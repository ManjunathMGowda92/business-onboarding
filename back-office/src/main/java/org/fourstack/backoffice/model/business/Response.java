package org.fourstack.backoffice.model.business;

import lombok.Builder;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
@Builder
public class Response implements Serializable {
    @Serial
    private static final long serialVersionUID = 7018982767846649843L;
    private String requestMsgId;
    private String result;
    private String errorCode;
    private String errorMsg;
    private String errorField;
}
