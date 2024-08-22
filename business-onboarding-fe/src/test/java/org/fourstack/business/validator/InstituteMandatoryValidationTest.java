package org.fourstack.business.validator;

import org.fourstack.business.BaseTest;
import org.fourstack.business.exceptions.MissingFieldException;
import org.fourstack.business.model.BusinessIdentifier;
import org.fourstack.business.model.BusinessRegisterRequest;
import org.fourstack.business.model.ValidationResult;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Collections;
import java.util.List;

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
        setTimeStamp(businessRequest.getCommonData());
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
        setTimeStamp(businessRequest.getCommonData());
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
        setTimeStamp(businessRequest.getCommonData());
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
        setTimeStamp(businessRequest.getCommonData());
        businessRequest.getInstitute().setDefaultB2bId(defaultB2bId);
        MissingFieldException exception = Assertions.assertThrows(MissingFieldException.class,
                () -> formatValidator.validateBusiness(businessRequest));
        assertMissingFieldException(exception, "institute.defaultB2bId");
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {""})
    @DisplayName("InstituteMandatoryValidations: Mandatory validation for institute.mccCode")
    void testInstituteMccCodeFailure(String mccCode) {
        BusinessRegisterRequest businessRequest = getBusinessRequest();
        setTimeStamp(businessRequest.getCommonData());
        businessRequest.getInstitute().setMccCode(mccCode);
        MissingFieldException exception = Assertions.assertThrows(MissingFieldException.class,
                () -> formatValidator.validateBusiness(businessRequest));
        assertMissingFieldException(exception, "institute.mccCode");
    }

    @Test
    @DisplayName("InstituteMandatoryValidations: Mandatory validation for institute.lei")
    void testInstituteLeiFailure() {
        BusinessRegisterRequest businessRequest = getBusinessRequest();
        setTimeStamp(businessRequest.getCommonData());
        businessRequest.getInstitute().setLei(null);
        MissingFieldException exception = Assertions.assertThrows(MissingFieldException.class,
                () -> formatValidator.validateBusiness(businessRequest));
        assertMissingFieldException(exception, "institute.lei");
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {""})
    @DisplayName("InstituteMandatoryValidations: Mandatory validation for institute.lei.documentName")
    void testInstituteLeiDocumentNameFailure(String documentName) {
        BusinessRegisterRequest businessRequest = getBusinessRequest();
        setTimeStamp(businessRequest.getCommonData());
        businessRequest.getInstitute().getLei().setDocumentName(documentName);
        MissingFieldException exception = Assertions.assertThrows(MissingFieldException.class,
                () -> formatValidator.validateBusiness(businessRequest));
        assertMissingFieldException(exception, "institute.lei.documentName");
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {""})
    @DisplayName("InstituteMandatoryValidations: Mandatory validation for institute.lei.type")
    void testInstituteLeiTypeFailure(String type) {
        BusinessRegisterRequest businessRequest = getBusinessRequest();
        setTimeStamp(businessRequest.getCommonData());
        businessRequest.getInstitute().getLei().setType(type);
        MissingFieldException exception = Assertions.assertThrows(MissingFieldException.class,
                () -> formatValidator.validateBusiness(businessRequest));
        assertMissingFieldException(exception, "institute.lei.type");
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {""})
    @DisplayName("InstituteMandatoryValidations: Mandatory validation for institute.lei.value")
    void testInstituteLeiValueFailure(String value) {
        BusinessRegisterRequest businessRequest = getBusinessRequest();
        setTimeStamp(businessRequest.getCommonData());
        businessRequest.getInstitute().getLei().setValue(value);
        MissingFieldException exception = Assertions.assertThrows(MissingFieldException.class,
                () -> formatValidator.validateBusiness(businessRequest));
        assertMissingFieldException(exception, "institute.lei.value");
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {""})
    @DisplayName("InstituteMandatoryValidations: Mandatory validation for institute.lei.registeredName")
    void testInstituteLeiRegisteredNameFailure(String registeredName) {
        BusinessRegisterRequest businessRequest = getBusinessRequest();
        setTimeStamp(businessRequest.getCommonData());
        businessRequest.getInstitute().getLei().setRegisteredName(registeredName);
        MissingFieldException exception = Assertions.assertThrows(MissingFieldException.class,
                () -> formatValidator.validateBusiness(businessRequest));
        assertMissingFieldException(exception, "institute.lei.registeredName");
    }

    @Test
    @DisplayName("InstituteMandatoryValidations: Mandatory validation for institute.primaryIdentifier")
    void testInstitutePrimaryIdentifierFailure() {
        BusinessRegisterRequest businessRequest = getBusinessRequest();
        setTimeStamp(businessRequest.getCommonData());
        businessRequest.getInstitute().setPrimaryIdentifier(null);
        MissingFieldException exception = Assertions.assertThrows(MissingFieldException.class,
                () -> formatValidator.validateBusiness(businessRequest));
        assertMissingFieldException(exception, "institute.primaryIdentifier");
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {""})
    @DisplayName("InstituteMandatoryValidations: Mandatory validation for institute.primaryIdentifier.documentName")
    void testInstitutePrimaryIdentifierDocumentNameFailure(String documentName) {
        BusinessRegisterRequest businessRequest = getBusinessRequest();
        setTimeStamp(businessRequest.getCommonData());
        businessRequest.getInstitute().getPrimaryIdentifier().setDocumentName(documentName);
        MissingFieldException exception = Assertions.assertThrows(MissingFieldException.class,
                () -> formatValidator.validateBusiness(businessRequest));
        assertMissingFieldException(exception, "institute.primaryIdentifier.documentName");
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {""})
    @DisplayName("InstituteMandatoryValidations: Mandatory validation for institute.primaryIdentifier.value")
    void testInstitutePrimaryIdentifierValueFailure(String value) {
        BusinessRegisterRequest businessRequest = getBusinessRequest();
        setTimeStamp(businessRequest.getCommonData());
        businessRequest.getInstitute().getPrimaryIdentifier().setValue(value);
        MissingFieldException exception = Assertions.assertThrows(MissingFieldException.class,
                () -> formatValidator.validateBusiness(businessRequest));
        assertMissingFieldException(exception, "institute.primaryIdentifier.value");
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {""})
    @DisplayName("InstituteMandatoryValidations: Mandatory validation for institute.primaryIdentifier.registeredName")
    void testInstitutePrimaryIdentifierRegisteredNameFailure(String registeredName) {
        BusinessRegisterRequest businessRequest = getBusinessRequest();
        setTimeStamp(businessRequest.getCommonData());
        businessRequest.getInstitute().getPrimaryIdentifier().setRegisteredName(registeredName);
        MissingFieldException exception = Assertions.assertThrows(MissingFieldException.class,
                () -> formatValidator.validateBusiness(businessRequest));
        assertMissingFieldException(exception, "institute.primaryIdentifier.registeredName");
    }

    @Test
    @DisplayName("InstituteMandatoryValidations: Mandatory validation for institute.otherIdentifiers")
    void testInstituteOtherIdentifiersSuccessForNull() {
        BusinessRegisterRequest businessRequest = getBusinessRequest();
        setTimeStamp(businessRequest.getCommonData());
        businessRequest.getInstitute().setOtherIdentifiers(null);
        ValidationResult result = formatValidator.validateBusiness(businessRequest);
        assertSuccessValidation(result);
    }

    @Test
    @DisplayName("InstituteMandatoryValidations: Mandatory validation for institute.otherIdentifiers")
    void testInstituteOtherIdentifiersSuccessForEmptyList() {
        BusinessRegisterRequest businessRequest = getBusinessRequest();
        setTimeStamp(businessRequest.getCommonData());
        businessRequest.getInstitute().setOtherIdentifiers(Collections.emptyList());
        ValidationResult result = formatValidator.validateBusiness(businessRequest);
        assertSuccessValidation(result);
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {""})
    @DisplayName("InstituteMandatoryValidations: Mandatory validation for institute.otherIdentifiers.documentName")
    void testInstituteOtherIdentifiersDocumentNameFailure(String documentName) {
        BusinessRegisterRequest businessRequest = getBusinessRequest();
        setTimeStamp(businessRequest.getCommonData());
        List<BusinessIdentifier> otherIdentifiers = businessRequest.getInstitute().getOtherIdentifiers();
        for (BusinessIdentifier otherIdentifier : otherIdentifiers) {
            otherIdentifier.setDocumentName(documentName);
            MissingFieldException exception = Assertions.assertThrows(MissingFieldException.class,
                    () -> formatValidator.validateBusiness(businessRequest));
            assertMissingFieldException(exception, "institute.otherIdentifiers.documentName");
        }
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {""})
    @DisplayName("InstituteMandatoryValidations: Mandatory validation for institute.otherIdentifiers.value")
    void testInstituteOtherIdentifiersValueFailure(String value) {
        BusinessRegisterRequest businessRequest = getBusinessRequest();
        setTimeStamp(businessRequest.getCommonData());
        List<BusinessIdentifier> otherIdentifiers = businessRequest.getInstitute().getOtherIdentifiers();
        for (BusinessIdentifier otherIdentifier : otherIdentifiers) {
            otherIdentifier.setValue(value);
            MissingFieldException exception = Assertions.assertThrows(MissingFieldException.class,
                    () -> formatValidator.validateBusiness(businessRequest));
            assertMissingFieldException(exception, "institute.otherIdentifiers.value");
        }
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {""})
    @DisplayName("InstituteMandatoryValidations: Mandatory validation for institute.otherIdentifiers.registeredName")
    void testInstituteOtherIdentifiersRegisteredNameFailure(String registeredName) {
        BusinessRegisterRequest businessRequest = getBusinessRequest();
        setTimeStamp(businessRequest.getCommonData());
        List<BusinessIdentifier> otherIdentifiers = businessRequest.getInstitute().getOtherIdentifiers();
        for (BusinessIdentifier otherIdentifier : otherIdentifiers) {
            otherIdentifier.setRegisteredName(registeredName);
            MissingFieldException exception = Assertions.assertThrows(MissingFieldException.class,
                    () -> formatValidator.validateBusiness(businessRequest));
            assertMissingFieldException(exception, "institute.otherIdentifiers.registeredName");
        }
    }
}
