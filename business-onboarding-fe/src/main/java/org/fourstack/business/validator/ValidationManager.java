package org.fourstack.business.validator;

import org.fourstack.business.model.Validation;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class ValidationManager {
    private static final Map<String, Validation> validationConfig = new HashMap<>();

    public static void createValidationConfig(String key, Validation validation) {
        validationConfig.put(key, validation);
    }

    public Validation getValidationConfig(String key) {
        return validationConfig.get(key);
    }
}
