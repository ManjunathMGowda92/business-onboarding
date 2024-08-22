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

class HeaderMandatoryValidationTest extends BaseTest {

    @Test
    @DisplayName("HeaderMandatoryValidations: Mandatory validation for commonData")
    void testCommonDataObjectFailure() {
        BusinessRegisterRequest businessRequest = getBusinessRequest();
        businessRequest.setCommonData(null);
        MissingFieldException exception = Assertions.assertThrows(MissingFieldException.class,
                () -> formatValidator.validateBusiness(businessRequest));
        assertMissingFieldException(exception, "commonData");
    }

    @Test
    @DisplayName("HeaderMandatoryValidations: Mandatory validation for commonData.head")
    void testHeaderFailure() {
        BusinessRegisterRequest businessRequest = getBusinessRequest();
        businessRequest.getCommonData().setHead(null);
        MissingFieldException exception = Assertions.assertThrows(MissingFieldException.class,
                () -> formatValidator.validateBusiness(businessRequest));
        assertMissingFieldException(exception, "commonData.head");
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {""})
    @DisplayName("HeaderMandatoryValidations: Mandatory validation for commonData.head.ver")
    void testHeaderVerFailure(String ver) {
        BusinessRegisterRequest businessRequest = getBusinessRequest();
        businessRequest.getCommonData().getHead().setVer(ver);
        MissingFieldException exception = Assertions.assertThrows(MissingFieldException.class,
                () -> formatValidator.validateBusiness(businessRequest));
        assertMissingFieldException(exception, "commonData.head.ver");

    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {""})
    @DisplayName("HeaderMandatoryValidations: Mandatory validation for commonData.head.ts")
    void testHeaderTimeStampFailure(String ts) {
        BusinessRegisterRequest businessRequest = getBusinessRequest();
        businessRequest.getCommonData().getHead().setTs(ts);
        MissingFieldException exception = Assertions.assertThrows(MissingFieldException.class,
                () -> formatValidator.validateBusiness(businessRequest));
        assertMissingFieldException(exception, "commonData.head.ts");
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {""})
    @DisplayName("HeaderMandatoryValidations: Mandatory validation for commonData.head.msgId")
    void testHeaderMsgIdFailure(String msgId) {
        BusinessRegisterRequest businessRequest = getBusinessRequest();
        businessRequest.getCommonData().getHead().setMsgId(msgId);
        MissingFieldException exception = Assertions.assertThrows(MissingFieldException.class,
                () -> formatValidator.validateBusiness(businessRequest));
        assertMissingFieldException(exception, "commonData.head.msgId");
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {""})
    @DisplayName("HeaderMandatoryValidations: Mandatory validation for commonData.head.aiId")
    void testHeaderAiIdFailure(String aiId) {
        BusinessRegisterRequest businessRequest = getBusinessRequest();
        businessRequest.getCommonData().getHead().setAiId(aiId);
        MissingFieldException exception = Assertions.assertThrows(MissingFieldException.class,
                () -> formatValidator.validateBusiness(businessRequest));
        assertMissingFieldException(exception, "commonData.head.aiId");
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {""})
    @DisplayName("HeaderMandatoryValidations: Mandatory validation for commonData.head.prodType")
    void testHeaderProductTypeFailure(String prodType) {
        BusinessRegisterRequest businessRequest = getBusinessRequest();
        businessRequest.getCommonData().getHead().setProdType(prodType);
        MissingFieldException exception = Assertions.assertThrows(MissingFieldException.class,
                () -> formatValidator.validateBusiness(businessRequest));
        assertMissingFieldException(exception, "commonData.head.prodType");
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {""})
    @DisplayName("HeaderMandatoryValidations: Mandatory validation for commonData.head.ouId")
    void testHeadOuIdFailure(String ouId) {
        BusinessRegisterRequest businessRequest = getBusinessRequest();
        businessRequest.getCommonData().getHead().setOuId(ouId);
        MissingFieldException exception = Assertions.assertThrows(MissingFieldException.class,
                () -> formatValidator.validateBusiness(businessRequest));
        assertMissingFieldException(exception, "commonData.head.ouId");
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {""})
    @DisplayName("HeaderMandatoryValidations: validation success for commonData.head.orgSysId")
    void testSuccessValidationForOrgSysIdNullOrEmpty(String orgSysId) {
        BusinessRegisterRequest businessRequest = getBusinessRequest();
        setTimeStamp(businessRequest.getCommonData());
        businessRequest.getCommonData().getHead().setOrgSysId(orgSysId);
        ValidationResult result = formatValidator.validateBusiness(businessRequest);
        assertSuccessValidation(result);
    }
}
