package org.fourstack.business.entity.event;

import lombok.Data;
import org.fourstack.business.enums.EventType;

import java.io.Serial;
import java.io.Serializable;

@Data
public class Message<R, A> implements Serializable {
    @Serial
    private static final long serialVersionUID = 7823147727254271046L;
    private Object request;
    private Object ack;
    private String identifierKey;
    private Class<R> requestObjType;
    private Class<A> ackObjType;
    private EventType eventType;
}
