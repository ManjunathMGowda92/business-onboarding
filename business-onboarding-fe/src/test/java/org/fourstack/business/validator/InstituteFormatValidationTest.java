package org.fourstack.business.validator;

import org.fourstack.business.BaseTest;
import org.fourstack.business.enums.ErrorCodeScenario;
import org.fourstack.business.enums.LeiType;
import org.fourstack.business.exceptions.ValidationException;
import org.fourstack.business.model.BusinessIdentifier;
import org.fourstack.business.model.BusinessRegisterRequest;
import org.fourstack.business.model.ValidationResult;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;

class InstituteFormatValidationTest extends BaseTest {

    @ParameterizedTest
    @ValueSource(strings = {"OBJ75468486468486", "ORGGMTX46A620I00D20240821T1028372830", "ORRGMTX46A620I00D20240821T102837283",
            "ORGGMTX46A620I00D20240821T10283)283", "ORGGMTX46A620I00D20240821T10ght#283", "ROGGMTX46A620I00D20240821T102837283"})
    @DisplayName("InstituteFormatValidationTest: Format validations for institute.objectId")
    void testObjectIdFailure(String objectId) {
        BusinessRegisterRequest businessRequest = getBusinessRequest();
        setTimeStamp(businessRequest.getCommonData());
        businessRequest.getInstitute().setObjectId(objectId);
        ValidationException exception = Assertions.assertThrows(ValidationException.class,
                () -> formatValidator.validateBusiness(businessRequest));
        assertValidationException(exception, ErrorCodeScenario.INPUT_0002, "institute.objectId");
    }

    @ParameterizedTest
    @ValueSource(strings = {"Company name \\", "My Company\" with data - 9867597", "My Company Data \"9787807\""})
    @DisplayName("InstituteFormatValidationTest: Format validations for institute.name")
    void testNameFailure(String name) {
        BusinessRegisterRequest businessRequest = getBusinessRequest();
        setTimeStamp(businessRequest.getCommonData());
        businessRequest.getInstitute().setName(name);
        ValidationException exception = Assertions.assertThrows(ValidationException.class,
                () -> formatValidator.validateBusiness(businessRequest));
        assertValidationException(exception, ErrorCodeScenario.INPUT_0002, "institute.name");
    }

    @Test
    @DisplayName("InstituteFormatValidationTest: Format validations for institute.name")
    void testNameFailure() {
        BusinessRegisterRequest businessRequest = getBusinessRequest();
        setTimeStamp(businessRequest.getCommonData());
        businessRequest.getInstitute().setName(alphaNumericString(257, ""));
        ValidationException exception = Assertions.assertThrows(ValidationException.class,
                () -> formatValidator.validateBusiness(businessRequest));
        assertValidationException(exception, ErrorCodeScenario.INPUT_0002, "institute.name");
    }

    @ParameterizedTest
    @ValueSource(strings = {"Company name \\", "My Company\" with data - 9867597", "My Company Data \"9787807\""})
    @DisplayName("InstituteFormatValidationTest: Format validations for institute.alias")
    void testAliasFailure(String alias) {
        BusinessRegisterRequest businessRequest = getBusinessRequest();
        setTimeStamp(businessRequest.getCommonData());
        businessRequest.getInstitute().setAlias(alias);
        ValidationException exception = Assertions.assertThrows(ValidationException.class,
                () -> formatValidator.validateBusiness(businessRequest));
        assertValidationException(exception, ErrorCodeScenario.INPUT_0002, "institute.alias");
    }

    @ParameterizedTest
    @ValueSource(strings = {"Company name \\", "My Company\" with data - 9867597", "My Company Data \"9787807\""})
    @DisplayName("InstituteFormatValidationTest: Format validations for institute.defaultB2bId")
    void testDefaultB2BIdFailure(String b2bId) {
        BusinessRegisterRequest businessRequest = getBusinessRequest();
        setTimeStamp(businessRequest.getCommonData());
        businessRequest.getInstitute().setDefaultB2bId(b2bId);
        ValidationException exception = Assertions.assertThrows(ValidationException.class,
                () -> formatValidator.validateBusiness(businessRequest));
        assertValidationException(exception, ErrorCodeScenario.INPUT_0002, "institute.defaultB2bId");
    }

    @Test
    @DisplayName("InstituteFormatValidationTest: Format validations for institute.defaultB2bId")
    void testDefaultB2BIdFailure() {
        BusinessRegisterRequest businessRequest = getBusinessRequest();
        setTimeStamp(businessRequest.getCommonData());
        String id = alphaNumericString(101, "ID");
        businessRequest.getInstitute().setDefaultB2bId(id);
        ValidationException exception = Assertions.assertThrows(ValidationException.class,
                () -> formatValidator.validateBusiness(businessRequest));
        assertValidationException(exception, ErrorCodeScenario.INPUT_0002, "institute.defaultB2bId");
    }

    @ParameterizedTest
    @ValueSource(strings = {"123", "00000", "98765", "9868969", "lkjh", "abcd", "099"})
    @DisplayName("InstituteFormatValidationTest: Format validations for institute.mccCode")
    void testMccCodeFailure(String mccCode) {
        BusinessRegisterRequest businessRequest = getBusinessRequest();
        setTimeStamp(businessRequest.getCommonData());
        businessRequest.getInstitute().setMccCode(mccCode);
        ValidationException exception = Assertions.assertThrows(ValidationException.class,
                () -> formatValidator.validateBusiness(businessRequest));
        assertValidationException(exception, ErrorCodeScenario.INPUT_0002, "institute.mccCode");
    }

    @ParameterizedTest
    @ValueSource(strings = {"123", "00000", "98765", "9868969", "lkjh", "abcd", "099", "pan", "PAn", "pAN"})
    @DisplayName("InstituteFormatValidationTest: Format validations for institute.lei.documentName")
    void testLeiDocumentNameFailure(String documentName) {
        BusinessRegisterRequest businessRequest = getBusinessRequest();
        setTimeStamp(businessRequest.getCommonData());
        businessRequest.getInstitute().getLei().setDocumentName(documentName);
        ValidationException exception = Assertions.assertThrows(ValidationException.class,
                () -> formatValidator.validateBusiness(businessRequest));
        assertValidationException(exception, ErrorCodeScenario.INPUT_0002, "institute.lei.documentName");
    }

    @ParameterizedTest
    @ValueSource(strings = {"sole prop", "company", "huf", "trust", "government agency", "SOLE PEOP", "SOLE PROP ",
            " COMPANY", "HUF ", " TRUST", "GOVERNMENT  AGENCY"})
    @DisplayName("InstituteFormatValidationTest: Format validations for institute.lei.type")
    void testLeiTypeFailure(String type) {
        BusinessRegisterRequest businessRequest = getBusinessRequest();
        setTimeStamp(businessRequest.getCommonData());
        businessRequest.getInstitute().getLei().setType(type);
        ValidationException exception = Assertions.assertThrows(ValidationException.class,
                () -> formatValidator.validateBusiness(businessRequest));
        assertValidationException(exception, ErrorCodeScenario.INPUT_0002, "institute.lei.type");
    }

    @ParameterizedTest
    @EnumSource
    @DisplayName("InstituteFormatValidationTest: Success validations for institute.lei.type")
    void testLeiTypeSuccess(LeiType type) {
        BusinessRegisterRequest businessRequest = getBusinessRequest();
        setTimeStamp(businessRequest.getCommonData());
        businessRequest.getInstitute().getLei().setType(type.getType());
        if (type.getType().equals("SOLE PROP")) {
            businessRequest.getInstitute().getLei().setValue("COHPM6543T");
        } else {
            businessRequest.getInstitute().getLei().setValue("COHUN6543T");
        }
        ValidationResult result = formatValidator.validateBusiness(businessRequest);
        assertSuccessValidation(result);
    }

    @ParameterizedTest
    @ValueSource(strings = {"ABCDRET", "87678HUY", "agfd765788", "cohpm6543t", "COHPM64YY"})
    @DisplayName("InstituteFormatValidationTest: Success validations for institute.lei.value")
    void testLeiValueFailure(String value) {
        BusinessRegisterRequest businessRequest = getBusinessRequest();
        setTimeStamp(businessRequest.getCommonData());
        businessRequest.getInstitute().getLei().setValue(value);
        ValidationException exception = Assertions.assertThrows(ValidationException.class,
                () -> formatValidator.validateBusiness(businessRequest));
        assertValidationException(exception, ErrorCodeScenario.INPUT_0002, "institute.lei.value");
    }

    @ParameterizedTest
    @ValueSource(strings = {"ABCDRET234", "87678HUY89", "OUTY9876TY", "COHRMPO64465Y", "COHPM649YY"})
    @DisplayName("InstituteFormatValidationTest: Success validations for institute.lei.value")
    void testLeiValuePanFormatFailure(String value) {
        BusinessRegisterRequest businessRequest = getBusinessRequest();
        setTimeStamp(businessRequest.getCommonData());
        businessRequest.getInstitute().getLei().setValue(value);
        ValidationException exception = Assertions.assertThrows(ValidationException.class,
                () -> formatValidator.validateBusiness(businessRequest));
        assertValidationException(exception, ErrorCodeScenario.BONB_0001, "institute.lei.value");
    }

    @ParameterizedTest
    @EnumSource
    @DisplayName("InstituteFormatValidationTest: Invalid Combinations check for institute.lei.value")
    void testInvalidLeiAndValueAndTypeCombination(LeiType type) {
        BusinessRegisterRequest businessRequest = getBusinessRequest();
        setTimeStamp(businessRequest.getCommonData());
        businessRequest.getInstitute().getLei().setType(type.getType());
        if (type.getType().equals("SOLE PROP")) {
            businessRequest.getInstitute().getLei().setValue("COHUN6543T");
        } else {
            businessRequest.getInstitute().getLei().setValue("COHPM6543T");
        }
        ValidationException exception = Assertions.assertThrows(ValidationException.class,
                () -> formatValidator.validateBusiness(businessRequest));
        assertValidationException(exception, ErrorCodeScenario.BONB_0002, "institute.lei.value");
    }

    @ParameterizedTest
    @ValueSource(strings = {"sole prop\\", "company\"", "\"huf\"", "\\trust", "government \"agency\""})
    @DisplayName("InstituteFormatValidationTest: Format validations for institute.lei.registeredName")
    void testLeiRegisteredNameFailure(String registeredName) {
        BusinessRegisterRequest businessRequest = getBusinessRequest();
        setTimeStamp(businessRequest.getCommonData());
        businessRequest.getInstitute().getLei().setRegisteredName(registeredName);
        ValidationException exception = Assertions.assertThrows(ValidationException.class,
                () -> formatValidator.validateBusiness(businessRequest));
        assertValidationException(exception, ErrorCodeScenario.INPUT_0002, "institute.lei.registeredName");
    }

    @Test
    @DisplayName("InstituteFormatValidationTest: Format validations for institute.lei.registeredName")
    void testLeiRegisteredNameFailure() {
        BusinessRegisterRequest businessRequest = getBusinessRequest();
        setTimeStamp(businessRequest.getCommonData());
        businessRequest.getInstitute().getLei().setRegisteredName(alphaNumericString(257, ""));
        ValidationException exception = Assertions.assertThrows(ValidationException.class,
                () -> formatValidator.validateBusiness(businessRequest));
        assertValidationException(exception, ErrorCodeScenario.INPUT_0002, "institute.lei.registeredName");
    }

    @ParameterizedTest
    @ValueSource(strings = {"sole prop\\", "company\"", "\"huf\"", "GSTIN ", " UDYAM", "SHOP  ESTABLISHMENT NUMBER", " TAN", "FSSAI ",
            "gstin", "udyam", "shop establishment number", "tan", "fssai"})
    @DisplayName("InstituteFormatValidationTest: Format validations for institute.primaryIdentifier.documentName")
    void testPrimaryIdentifierDocumentNameFailure(String documentName) {
        BusinessRegisterRequest businessRequest = getBusinessRequest();
        setTimeStamp(businessRequest.getCommonData());
        businessRequest.getInstitute().getPrimaryIdentifier().setDocumentName(documentName);
        ValidationException exception = Assertions.assertThrows(ValidationException.class,
                () -> formatValidator.validateBusiness(businessRequest));
        assertValidationException(exception, ErrorCodeScenario.INPUT_0002, "institute.primaryIdentifier.documentName");
    }

    @ParameterizedTest
    @ValueSource(strings = {"GSTIN", "UDYAM", "SHOP ESTABLISHMENT NUMBER", "TAN", "FSSAI"})
    @DisplayName("InstituteFormatValidationTest: Success validations for institute.primaryIdentifier.documentName")
    void testPrimaryIdentifierDocumentNameSuccess(String documentName) {
        BusinessRegisterRequest businessRequest = getBusinessRequest();
        setTimeStamp(businessRequest.getCommonData());
        businessRequest.getInstitute().getPrimaryIdentifier().setDocumentName(documentName);
        ValidationResult result = formatValidator.validateBusiness(businessRequest);
        assertSuccessValidation(result);
    }

    @ParameterizedTest
    @ValueSource(strings = {"sole prop\\", "company\"", "\"huf\"", "GSTIN \\", "shop \"establishment\" number", "tan\\", "\"fssai"})
    @DisplayName("InstituteFormatValidationTest: Format validations for institute.primaryIdentifier.value")
    void testPrimaryIdentifierValueFailure(String value) {
        BusinessRegisterRequest businessRequest = getBusinessRequest();
        setTimeStamp(businessRequest.getCommonData());
        businessRequest.getInstitute().getPrimaryIdentifier().setValue(value);
        ValidationException exception = Assertions.assertThrows(ValidationException.class,
                () -> formatValidator.validateBusiness(businessRequest));
        assertValidationException(exception, ErrorCodeScenario.INPUT_0002, "institute.primaryIdentifier.value");
    }

    @Test
    @DisplayName("InstituteFormatValidationTest: Format validations for institute.primaryIdentifier.value")
    void testPrimaryIdentifierValueFailure() {
        BusinessRegisterRequest businessRequest = getBusinessRequest();
        setTimeStamp(businessRequest.getCommonData());
        businessRequest.getInstitute().getPrimaryIdentifier().setValue(alphaNumericString(101, ""));
        ValidationException exception = Assertions.assertThrows(ValidationException.class,
                () -> formatValidator.validateBusiness(businessRequest));
        assertValidationException(exception, ErrorCodeScenario.INPUT_0002, "institute.primaryIdentifier.value");
    }

    @ParameterizedTest
    @ValueSource(strings = {"sole prop\\", "company\"", "\"huf\"", "GSTIN \\", "shop \"establishment\" number", "tan\\", "\"fssai"})
    @DisplayName("InstituteFormatValidationTest: Format validations for institute.primaryIdentifier.registeredName")
    void testPrimaryIdentifierRegisteredNameFailure(String registeredName) {
        BusinessRegisterRequest businessRequest = getBusinessRequest();
        setTimeStamp(businessRequest.getCommonData());
        businessRequest.getInstitute().getPrimaryIdentifier().setRegisteredName(registeredName);
        ValidationException exception = Assertions.assertThrows(ValidationException.class,
                () -> formatValidator.validateBusiness(businessRequest));
        assertValidationException(exception, ErrorCodeScenario.INPUT_0002, "institute.primaryIdentifier.registeredName");
    }

    @Test
    @DisplayName("InstituteFormatValidationTest: Format validations for institute.primaryIdentifier.registeredName")
    void testPrimaryIdentifierRegisteredNameFailure() {
        BusinessRegisterRequest businessRequest = getBusinessRequest();
        setTimeStamp(businessRequest.getCommonData());
        businessRequest.getInstitute().getPrimaryIdentifier().setRegisteredName(alphaNumericString(257, ""));
        ValidationException exception = Assertions.assertThrows(ValidationException.class,
                () -> formatValidator.validateBusiness(businessRequest));
        assertValidationException(exception, ErrorCodeScenario.INPUT_0002, "institute.primaryIdentifier.registeredName");
    }

    @ParameterizedTest
    @ValueSource(strings = {"sole prop\\", "company\"", "\"huf\"", "GSTIN ", " UDYAM", "SHOP  ESTABLISHMENT NUMBER", " TAN", "FSSAI ",
            "gstin", "udyam", "shop establishment number", "tan", "fssai"})
    @DisplayName("InstituteFormatValidationTest: Format validations for institute.otherIdentifiers.documentName")
    void testOtherIdentifiersDocumentNameFailure(String documentName) {
        BusinessRegisterRequest businessRequest = getBusinessRequest();
        setTimeStamp(businessRequest.getCommonData());
        List<BusinessIdentifier> otherIdentifiers = businessRequest.getInstitute().getOtherIdentifiers();
        for (BusinessIdentifier otherIdentifier : otherIdentifiers) {
            otherIdentifier.setDocumentName(documentName);
            ValidationException exception = Assertions.assertThrows(ValidationException.class,
                    () -> formatValidator.validateBusiness(businessRequest));
            assertValidationException(exception, ErrorCodeScenario.INPUT_0002, "institute.otherIdentifiers.documentName");
        }
    }

    @ParameterizedTest
    @ValueSource(strings = {"sole prop\\", "company\"", "\"huf\"", "GSTIN \\", "shop \"establishment\" number", "tan\\", "\"fssai"})
    @DisplayName("InstituteFormatValidationTest: Format validations for institute.otherIdentifiers.value")
    void testOtherIdentifiersValueFailure(String value) {
        BusinessRegisterRequest businessRequest = getBusinessRequest();
        setTimeStamp(businessRequest.getCommonData());
        List<BusinessIdentifier> otherIdentifiers = businessRequest.getInstitute().getOtherIdentifiers();
        for (BusinessIdentifier otherIdentifier : otherIdentifiers) {
            otherIdentifier.setValue(value);
            ValidationException exception = Assertions.assertThrows(ValidationException.class,
                    () -> formatValidator.validateBusiness(businessRequest));
            assertValidationException(exception, ErrorCodeScenario.INPUT_0002, "institute.otherIdentifiers.value");
        }
    }

    @Test
    @DisplayName("InstituteFormatValidationTest: Format validations for institute.otherIdentifiers.value")
    void testOtherIdentifiersValueFailure() {
        BusinessRegisterRequest businessRequest = getBusinessRequest();
        setTimeStamp(businessRequest.getCommonData());
        List<BusinessIdentifier> otherIdentifiers = businessRequest.getInstitute().getOtherIdentifiers();
        for (BusinessIdentifier otherIdentifier : otherIdentifiers) {
            otherIdentifier.setValue(alphaNumericString(101, ""));
            ValidationException exception = Assertions.assertThrows(ValidationException.class,
                    () -> formatValidator.validateBusiness(businessRequest));
            assertValidationException(exception, ErrorCodeScenario.INPUT_0002, "institute.otherIdentifiers.value");
        }
    }

    @ParameterizedTest
    @ValueSource(strings = {"sole prop\\", "company\"", "\"huf\"", "GSTIN \\", "shop \"establishment\" number", "tan\\", "\"fssai"})
    @DisplayName("InstituteFormatValidationTest: Format validations for institute.otherIdentifiers.registeredName")
    void testOtherIdentifiersRegisteredNameFailure(String registeredName) {
        BusinessRegisterRequest businessRequest = getBusinessRequest();
        setTimeStamp(businessRequest.getCommonData());
        List<BusinessIdentifier> otherIdentifiers = businessRequest.getInstitute().getOtherIdentifiers();
        for (BusinessIdentifier otherIdentifier : otherIdentifiers) {
            otherIdentifier.setRegisteredName(registeredName);
            ValidationException exception = Assertions.assertThrows(ValidationException.class,
                    () -> formatValidator.validateBusiness(businessRequest));
            assertValidationException(exception, ErrorCodeScenario.INPUT_0002, "institute.otherIdentifiers.registeredName");
        }
    }

    @Test
    @DisplayName("InstituteFormatValidationTest: Format validations for institute.otherIdentifiers.registeredName")
    void testOtherIdentifiersRegisteredNameFailure() {
        BusinessRegisterRequest businessRequest = getBusinessRequest();
        setTimeStamp(businessRequest.getCommonData());
        List<BusinessIdentifier> otherIdentifiers = businessRequest.getInstitute().getOtherIdentifiers();
        for (BusinessIdentifier otherIdentifier : otherIdentifiers) {
            otherIdentifier.setRegisteredName(alphaNumericString(257, ""));
            ValidationException exception = Assertions.assertThrows(ValidationException.class,
                    () -> formatValidator.validateBusiness(businessRequest));
            assertValidationException(exception, ErrorCodeScenario.INPUT_0002, "institute.otherIdentifiers.registeredName");
        }
    }

}
