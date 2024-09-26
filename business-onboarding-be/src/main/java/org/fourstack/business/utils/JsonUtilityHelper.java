package org.fourstack.business.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.fourstack.business.exception.ObjectMappingException;

import java.util.Objects;

public final class JsonUtilityHelper {
    private JsonUtilityHelper() {
    }

    private static ObjectMapper objectMapper;

    public static ObjectMapper getInstance() {
        if (Objects.isNull(objectMapper)) {
            synchronized (BusinessUtil.class) {
                if (Objects.isNull(objectMapper)) {
                    objectMapper = new ObjectMapper();
                }
            }
        }
        return objectMapper;
    }

    public static String convertToString(Object obj) throws ObjectMappingException {
        try {
            return getInstance().writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new ObjectMappingException("Object conversion failed - Object to toString : " + e.getMessage(), "", e);
        }
    }

    public static <T> T convertToObject(String source, Class<T> classObj) throws ObjectMappingException {
        try {
            return getInstance().readValue(source, classObj);
        } catch (JsonProcessingException e) {
            throw new ObjectMappingException("Object conversion failed - String to object : " + e.getMessage(), "", e);
        }
    }

    public static <T> T convertValue(Object inputValue, Class<T> targetType) {
        return getInstance().convertValue(inputValue, targetType);
    }
}
