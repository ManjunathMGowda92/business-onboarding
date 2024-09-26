package org.fourstack.business.entity.event;

import lombok.Data;
import org.fourstack.business.enums.EventType;

@Data
public class Message<R, A> {
    private Object request;
    private Object ack;
    private String identifierKey;
    private Class<R> requestObjType;
    private Class<A> ackObjType;
    private EventType eventType;
}
