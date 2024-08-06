package org.fourstack.business.validator;

import org.fourstack.business.model.ValidationResult;

public interface Validator<T> {

    ValidationResult validate(String key, T object);
}
