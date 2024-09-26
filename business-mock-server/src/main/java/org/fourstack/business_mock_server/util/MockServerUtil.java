package org.fourstack.business_mock_server.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Objects;

public final class MockServerUtil {
    private MockServerUtil(){
    }

    private static ObjectMapper objectMapper;

    private static ObjectMapper getInstance() {
        if (Objects.isNull(objectMapper)) {
            synchronized (MockServerUtil.class) {
                if (Objects.isNull(objectMapper)) {
                    objectMapper = new ObjectMapper();
                }
            }
        }
        return objectMapper;
    }

    public static String convertToString(Object obj) {
        try {
            return getInstance().writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
