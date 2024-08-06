package org.fourstack.business.validator;

import org.fourstack.business.BaseTest;
import org.fourstack.business.exceptions.MissingFieldException;
import org.fourstack.business.model.BusinessRegisterRequest;
import org.fourstack.business.model.ValidationResult;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

class InstituteMandatoryValidationTest extends BaseTest {

    @Test
    @DisplayName("InstituteMandatoryValidations: Mandatory validation for institute")
    void testNullInstituteFailure() {
        BusinessRegisterRequest businessRequest = getBusinessRequest();
        businessRequest.setInstitute(null);
        MissingFieldException exception = Assertions.assertThrows(MissingFieldException.class,
                () -> formatValidator.validateBusiness(businessRequest));
        assertMissingFieldException(exception, "institute");
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {""})
    @DisplayName("InstituteMandatoryValidations: Mandatory validation for institute.objectId")
    void testInstituteObjectIdFailure(String objectId) {
        BusinessRegisterRequest businessRequest = getBusinessRequest();
        businessRequest.getInstitute().setObjectId(objectId);
        MissingFieldException exception = Assertions.assertThrows(MissingFieldException.class,
                () -> formatValidator.validateBusiness(businessRequest));
        assertMissingFieldException(exception, "institute.objectId");
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {""})
    @DisplayName("InstituteMandatoryValidations: Mandatory validation for institute.name")
    void testInstituteNameFailure(String name) {
        BusinessRegisterRequest businessRequest = getBusinessRequest();
        businessRequest.getInstitute().setName(name);
        MissingFieldException exception = Assertions.assertThrows(MissingFieldException.class,
                () -> formatValidator.validateBusiness(businessRequest));
        assertMissingFieldException(exception, "institute.name");
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {""})
    @DisplayName("InstituteMandatoryValidations: Success validation for institute.alias")
    void testInstituteAliasSuccessWhenNullOrEmpty(String alias) {
        BusinessRegisterRequest businessRequest = getBusinessRequest();
        businessRequest.getInstitute().setAlias(alias);
        ValidationResult result = formatValidator.validateBusiness(businessRequest);
        assertSuccessValidation(result);
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {""})
    @DisplayName("InstituteMandatoryValidations: Mandatory validation for institute.defaultB2bId")
    void testInstituteDefaultB2BIdFailure(String defaultB2bId) {
        BusinessRegisterRequest businessRequest = getBusinessRequest();
        businessRequest.getInstitute().setDefaultB2bId(defaultB2bId);
        MissingFieldException exception = Assertions.assertThrows(MissingFieldException.class,
                () -> formatValidator.validateBusiness(businessRequest));
        assertMissingFieldException(exception, "institute.defaultB2bId");
    }
}
