package org.fourstack.business.entity.event;

import lombok.Data;
import org.fourstack.business.enums.EventType;

import java.io.Serial;
import java.io.Serializable;

@Data
public class Message<R, A> implements Serializable {
    @Serial
    private static final long serialVersionUID = 7823147727254271046L;
    private R request;
    private A ack;
    private String identifierKey;
    private Class<?> requestObjType;
    private Class<?> ackObjType;
    private EventType eventType;
}
