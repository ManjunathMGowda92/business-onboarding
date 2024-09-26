package org.fourstack.business.entity.event;

import lombok.Data;
import org.fourstack.business.enums.EventType;

@Data
public class Message<R, A> {
    private R request;
    private A ack;
    private String identifierKey;
    private Class<?> requestObjType;
    private Class<?> ackObjType;
    private EventType eventType;
}
