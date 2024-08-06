package org.fourstack.business.validator;

import org.fourstack.business.BaseTest;
import org.fourstack.business.enums.ErrorCodeScenario;
import org.fourstack.business.exceptions.MissingFieldException;
import org.fourstack.business.exceptions.ValidationException;
import org.fourstack.business.model.BusinessRegisterRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

class DeviceValidationsTest extends BaseTest {

    @ParameterizedTest
    @ValueSource(strings = {"GEOCODe", "Ip", "AGENT_Id", "234.5432", "abc.def", "*65.765",
            "agent_id", "geocode", "ip"})
    @DisplayName("DeviceValidation: format validations for commonData.device.tag.name")
    void testDeviceTagNameValidationFailure(String name) {
        BusinessRegisterRequest businessRequest = getBusinessRequest();
        businessRequest.getCommonData().getDevice().getTag().setName(name);
        ValidationException validationException = Assertions.assertThrows(ValidationException.class,
                () -> formatValidator.validateBusiness(businessRequest));
        assertValidationException(validationException, ErrorCodeScenario.INPUT_0002, "commonData.device.tag.name");
    }

    @ParameterizedTest
    @ValueSource(strings = {"GEOCO\\De", "\"Ip", "AGENT_Id\\", "\"234.5432", "abc\\.def", "*65.765\\",
            "agent_id\\", "\"geocode\"", "ip\\ counter \""})
    @DisplayName("DeviceValidation: format validations for commonData.device.tag.value")
    void testDeviceTagValueValidationFailure(String value) {
        BusinessRegisterRequest businessRequest = getBusinessRequest();
        businessRequest.getCommonData().getDevice().getTag().setValue(value);
        ValidationException validationException = Assertions.assertThrows(ValidationException.class,
                () -> formatValidator.validateBusiness(businessRequest));
        assertValidationException(validationException, ErrorCodeScenario.INPUT_0002, "commonData.device.tag.value");
    }

    @Test
    @DisplayName("DeviceValidation: Missing filed validation for commonData.device")
    void testDeviceValidationFailure() {
        BusinessRegisterRequest businessRequest = getBusinessRequest();
        businessRequest.getCommonData().setDevice(null);
        MissingFieldException exception = Assertions.assertThrows(MissingFieldException.class,
                () -> formatValidator.validateBusiness(businessRequest));
        assertMissingFieldException(exception, "commonData.device");
    }

    @Test
    @DisplayName("DeviceValidation: Missing filed validation for commonData.device.tag")
    void testDeviceTagValidationFailure() {
        BusinessRegisterRequest businessRequest = getBusinessRequest();
        businessRequest.getCommonData().getDevice().setTag(null);
        MissingFieldException exception = Assertions.assertThrows(MissingFieldException.class,
                () -> formatValidator.validateBusiness(businessRequest));
        assertMissingFieldException(exception, "commonData.device.tag");
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {""})
    @DisplayName("DeviceValidation: Missing filed validation for commonData.device.tag.name")
    void testDeviceTagNameMandatoryValidationFailure(String name) {
        BusinessRegisterRequest businessRequest = getBusinessRequest();
        businessRequest.getCommonData().getDevice().getTag().setName(name);
        MissingFieldException exception = Assertions.assertThrows(MissingFieldException.class,
                () -> formatValidator.validateBusiness(businessRequest));
        assertMissingFieldException(exception, "commonData.device.tag.name");
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {""})
    @DisplayName("DeviceValidation: Missing filed validation for commonData.device.tag.value")
    void testDeviceTagValueMandatoryValidationFailure(String value) {
        BusinessRegisterRequest businessRequest = getBusinessRequest();
        businessRequest.getCommonData().getDevice().getTag().setValue(value);
        MissingFieldException exception = Assertions.assertThrows(MissingFieldException.class,
                () -> formatValidator.validateBusiness(businessRequest));
        assertMissingFieldException(exception, "commonData.device.tag.value");
    }
}
