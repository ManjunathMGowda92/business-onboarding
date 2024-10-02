package org.fourstack.backoffice.model;

import lombok.Data;

import java.util.List;

@Data
public class BackOfficeListResponse {
    private List<?> responseList;
    private BackOfficeAck ack;
}
