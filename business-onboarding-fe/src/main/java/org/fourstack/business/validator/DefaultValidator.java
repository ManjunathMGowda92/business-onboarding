package org.fourstack.business.validator;

import org.apache.commons.beanutils.PropertyUtils;
import org.fourstack.business.exceptions.MissingFieldException;
import org.fourstack.business.exceptions.ValidationException;
import org.fourstack.business.model.RuleConfiguration;
import org.fourstack.business.model.Validation;
import org.fourstack.business.model.ValidationResult;
import org.fourstack.business.model.ValidationRule;
import org.fourstack.business.utils.BusinessUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.regex.Pattern;

public class DefaultValidator<T> implements Validator<T> {
    private static final Logger logger = LoggerFactory.getLogger(DefaultValidator.class);
    private final ValidationManager validationManager;

    public DefaultValidator() {
        this.validationManager = new ValidationManager();
    }

    @Override
    public ValidationResult validate(String key, T object) {
        return this.validateObject(key, object);
    }

    private ValidationResult validateObject(String key, T object) {
        Validation validationConfig = validationManager.getValidationConfig(key);
        if (BusinessUtil.isNotNull(validationConfig)) {
            List<ValidationRule> rules = validationConfig.getRule();
            return verify(object, rules);
        }
        return BusinessUtil.generateSuccessValidation();
    }

    private ValidationResult verify(T object, List<ValidationRule> rules) {
        for (ValidationRule rule : rules) {
            RuleConfiguration<T> ruleConfig = new RuleConfiguration<>(object, rule);
            boolean isNotValid = validateRuleConfig(ruleConfig);
            if (isNotValid) {
                throw new ValidationException("Validation failure for the field: " + rule.getFieldName(), rule.getErrorCode(),
                        rule.getErrorMessage(), rule.getFieldName());
            }
        }
        return BusinessUtil.generateSuccessValidation();
    }

    private boolean validateRuleConfig(RuleConfiguration<T> ruleConfig) {
        ValidationRule config = ruleConfig.config();
        return isNotMatch(ruleConfig.object(), config);
    }

    private boolean isNotMatch(T object, ValidationRule config) {
        try {
            Object property = PropertyUtils.getProperty(object, config.getIdentifier());
            String isOptional = config.getIsOptional();
            // If the field is optional and is not populated, then skip the pattern matching.
            if (BusinessUtil.isNotNull(isOptional) && "yes".equalsIgnoreCase(isOptional) && BusinessUtil.isNull(property)) {
                return false;
            }

            // if the field is required and is not populated, then throw MissingFieldException
            if ((BusinessUtil.isNull(isOptional) || !"yes".equalsIgnoreCase(isOptional))
                    && BusinessUtil.isObjectNullOrEmpty(property)) {
                throw new MissingFieldException("Missing required field", config.getFieldName());
            }
            Pattern pattern = Pattern.compile(config.getExpression());
            return doesNotMatch(pattern, property);
        } catch (MissingFieldException e) {
            throw e;
        } catch (Exception e) {
            logger.error("{} - Exception occurred in isMatch() method", this.getClass().getSimpleName());
            return true;
        }
    }

    private boolean doesNotMatch(Pattern pattern, Object object) {
        return object == null || !pattern.matcher(String.valueOf(object)).matches();
    }
}
