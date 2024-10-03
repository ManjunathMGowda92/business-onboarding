package org.fourstack.backoffice.model;

import lombok.Data;

@Data
public class BackOfficeResponse {
    private Object response;
    private BackOfficeAck ack;
}
