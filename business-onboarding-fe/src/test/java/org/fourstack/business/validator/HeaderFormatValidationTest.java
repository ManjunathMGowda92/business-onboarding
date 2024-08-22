package org.fourstack.business.validator;

import org.fourstack.business.BaseTest;
import org.fourstack.business.enums.ErrorCodeScenario;
import org.fourstack.business.exceptions.ValidationException;
import org.fourstack.business.model.BusinessRegisterRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


class HeaderFormatValidationTest extends BaseTest {

    @ParameterizedTest
    @ValueSource(strings = {"1", "1.2345", "23", "234.5432", "abc.def", "*65.765"})
    @DisplayName("BusinessHeaderValidation: format validations for commonData.head.ver")
    void testHeadVerValidationFailure(String ver) {
        BusinessRegisterRequest businessRequest = getBusinessRequest();
        businessRequest.getCommonData().getHead().setVer(ver);
        ValidationException validationException = Assertions.assertThrows(ValidationException.class,
                () -> formatValidator.validateBusiness(businessRequest));
        assertValidationException(validationException, ErrorCodeScenario.INPUT_0002, "commonData.head.ver");
    }

    @ParameterizedTest
    @ValueSource(strings = {"1", "1.2345", "23", "234.5432", "abc.def", "*65.765", "2024-08-91T09:10:42.6279215+05:30"})
    @DisplayName("BusinessHeaderValidation: format validations for commonData.head.ts")
    void testHeadTsValidationFailure(String timestamp) {
        BusinessRegisterRequest businessRequest = getBusinessRequest();
        businessRequest.getCommonData().getHead().setTs(timestamp);
        ValidationException validationException = Assertions.assertThrows(ValidationException.class,
                () -> formatValidator.validateBusiness(businessRequest));
        assertValidationException(validationException, ErrorCodeScenario.INPUT_0002, "commonData.head.ts");
    }

    @ParameterizedTest
    @ValueSource(strings = {"2024-08-20T09:10:42.6279215+05:30", "2024-08-21T09:10:42.6279215+05:30"})
    @DisplayName("BusinessHeaderValidation: format validations for commonData.head.ts")
    void testHeadTsDateCheckValidationFailure(String timestamp) {
        BusinessRegisterRequest businessRequest = getBusinessRequest();
        businessRequest.getCommonData().getHead().setTs(timestamp);
        ValidationException validationException = Assertions.assertThrows(ValidationException.class,
                () -> formatValidator.validateBusiness(businessRequest));
        assertValidationException(validationException, ErrorCodeScenario.TXN_0005, "commonData.head.ts");
    }

    @Test
    @DisplayName("BusinessHeaderValidation: format validations for commonData.head.ts")
    void testHeadTsPreviousDateCheckValidationFailure() {
        BusinessRegisterRequest businessRequest = getBusinessRequest();
        businessRequest.getCommonData().getHead().setTs(LocalDateTime.now().minusHours(25).format(DateTimeFormatter.ISO_DATE_TIME));
        ValidationException validationException = Assertions.assertThrows(ValidationException.class,
                () -> formatValidator.validateBusiness(businessRequest));
        assertValidationException(validationException, ErrorCodeScenario.TXN_0005, "commonData.head.ts");
    }

    @Test
    @DisplayName("BusinessHeaderValidation: format validations for commonData.head.ts")
    void testHeadTsNextDateCheckValidationFailure() {
        BusinessRegisterRequest businessRequest = getBusinessRequest();
        businessRequest.getCommonData().getHead().setTs(LocalDateTime.now().plusHours(25).format(DateTimeFormatter.ISO_DATE_TIME));
        ValidationException validationException = Assertions.assertThrows(ValidationException.class,
                () -> formatValidator.validateBusiness(businessRequest));
        assertValidationException(validationException, ErrorCodeScenario.TXN_0005, "commonData.head.ts");
    }

    @ParameterizedTest
    @ValueSource(strings = {"MSG8767595", "547647647MSG", "87%6YRYMSG", "MSGGMTS64C8B9H1*8(0240731T224737243",
            "MGSGMTSh6ae9h3e7i20240731T224523200", "MSGGMTSh6ae9h3e7i20240731T2245232008", "^&%GMTSh6ae9h3e7i20240731T224523200"})
    @DisplayName("BusinessHeaderValidation: format validations for commonData.head.msgId")
    void testHeadMsgIdValidationFailure(String msgId) {
        BusinessRegisterRequest businessRequest = getBusinessRequest();
        businessRequest.getCommonData().getHead().setMsgId(msgId);
        ValidationException validationException = Assertions.assertThrows(ValidationException.class,
                () -> formatValidator.validateBusiness(businessRequest));
        assertValidationException(validationException, ErrorCodeScenario.INPUT_0002, "commonData.head.msgId");
    }

    @ParameterizedTest
    @ValueSource(strings = {"AI099", "AI9", "ai09", "AIid", "9", "{AI}", "(AI8", "!234", "12345", "ANJSY"})
    @DisplayName("BusinessHeaderValidation: format validations for commonData.head.aiId")
    void testHeadAiIdValidations(String aiId) {
        BusinessRegisterRequest businessRequest = getBusinessRequest();
        businessRequest.getCommonData().getHead().setAiId(aiId);
        ValidationException validationException = Assertions.assertThrows(ValidationException.class,
                () -> formatValidator.validateBusiness(businessRequest));
        assertValidationException(validationException, ErrorCodeScenario.INPUT_0002, "commonData.head.aiId");
    }

    @ParameterizedTest
    @ValueSource(strings = {"AI099", "AI9", "ai09", "AIid", "9", "{AI}", "(AI8", "!234", "12345", "ANJSY", "O^75", "%^$&"})
    @DisplayName("BusinessHeaderValidation: format validations for commonData.head.ouId")
    void testHeadOuIdValidations(String ouId) {
        BusinessRegisterRequest businessRequest = getBusinessRequest();
        businessRequest.getCommonData().getHead().setOuId(ouId);
        ValidationException validationException = Assertions.assertThrows(ValidationException.class,
                () -> formatValidator.validateBusiness(businessRequest));
        assertValidationException(validationException, ErrorCodeScenario.INPUT_0002, "commonData.head.ouId");
    }

    @ParameterizedTest
    @ValueSource(strings = {"AI0-99", "AI9", "ai09+", "AI*id", "9", "{AI}", "(AI8", "!234", "12!345",
            "A@NJSY", "O^75", "%^$&", "1234657876%", "TYUR8765*", "Org System"})
    @DisplayName("BusinessHeaderValidation: format validations for commonData.head.orgSysId")
    void testHeadOrgSysIdValidations(String orgSysId) {
        BusinessRegisterRequest businessRequest = getBusinessRequest();
        businessRequest.getCommonData().getHead().setOrgSysId(orgSysId);
        ValidationException validationException = Assertions.assertThrows(ValidationException.class,
                () -> formatValidator.validateBusiness(businessRequest));
        assertValidationException(validationException, ErrorCodeScenario.INPUT_0002, "commonData.head.orgSysId");
    }

    @ParameterizedTest
    @ValueSource(strings = {"AI0-99", "AI9", "ai09+", "AI*id", "9", "{AI}", "(AI8", "!234", "12!345",
            "A@NJSY", "O^75", "%^$&", "1234657876%", "TYUR8765*", "Org System", "business", "BuSINESS"})
    @DisplayName("BusinessHeaderValidation: format validations for commonData.head.prodType")
    void testHeadProdTypeValidations(String prodType) {
        BusinessRegisterRequest businessRequest = getBusinessRequest();
        businessRequest.getCommonData().getHead().setProdType(prodType);
        ValidationException validationException = Assertions.assertThrows(ValidationException.class,
                () -> formatValidator.validateBusiness(businessRequest));
        assertValidationException(validationException, ErrorCodeScenario.INPUT_0002, "commonData.head.prodType");
    }
}
