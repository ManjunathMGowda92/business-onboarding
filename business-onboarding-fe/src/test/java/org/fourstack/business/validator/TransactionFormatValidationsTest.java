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

class TransactionFormatValidationsTest extends BaseTest {

    @ParameterizedTest
    @ValueSource(strings = {"87687", "tnf098970", "TXNGMTS64C8B9H1*8(0240731T224737243", "TXNGMTS64C8B9H14800240731T2247372431",
            "txnGMTS64C8B9H14800240731T2247372431", "TNXGMTS64C8B9H14800240731T2247372431"})
    @DisplayName("TransactionValidations: format validations for commonData.txn.id")
    void testTxnIdValidationFailure(String id) {
        BusinessRegisterRequest businessRequest = getBusinessRequest();
        businessRequest.getCommonData().getTxn().setId(id);
        ValidationException validationException = Assertions.assertThrows(ValidationException.class,
                () -> formatValidator.validateBusiness(businessRequest));
        assertValidationException(validationException, ErrorCodeScenario.INPUT_0002, "commonData.txn.id");
    }

    @ParameterizedTest
    @ValueSource(strings = {"87687", "tnf098970", "TXNGMTS64C8B9H1*8(0240731T224737243", "2024-08-21T39:10:42.6279215+05:30"})
    @DisplayName("TransactionValidations: format validations for commonData.txn.ts")
    void testTxnTsValidationFailure(String timestamp) {
        BusinessRegisterRequest businessRequest = getBusinessRequest();
        businessRequest.getCommonData().getTxn().setId(timestamp);
        ValidationException validationException = Assertions.assertThrows(ValidationException.class,
                () -> formatValidator.validateBusiness(businessRequest));
        assertValidationException(validationException, ErrorCodeScenario.INPUT_0002, "commonData.txn.id");
    }

    @Test
    @DisplayName("TransactionValidations: format validations for commonData.txn.ts")
    void testTxnTsValidationFailureForAheadTime() {
        BusinessRegisterRequest businessRequest = getBusinessRequest();
        setTimeStamp(businessRequest.getCommonData());
        businessRequest.getCommonData().getTxn().setTs(LocalDateTime.now().plusMinutes(5).format(DateTimeFormatter.ISO_DATE_TIME));
        ValidationException validationException = Assertions.assertThrows(ValidationException.class,
                () -> formatValidator.validateBusiness(businessRequest));
        assertValidationException(validationException, ErrorCodeScenario.TXN_0004, "commonData.txn.ts");
    }


    @Test
    @DisplayName("TransactionValidations: format validations for commonData.txn.ts")
    void testTxnTsValidationFailureForPreviousTime() {
        BusinessRegisterRequest businessRequest = getBusinessRequest();
        setTimeStamp(businessRequest.getCommonData());
        businessRequest.getCommonData().getTxn().setTs(LocalDateTime.now().minusMinutes(4).format(DateTimeFormatter.ISO_DATE_TIME));
        ValidationException validationException = Assertions.assertThrows(ValidationException.class,
                () -> formatValidator.validateBusiness(businessRequest));
        assertValidationException(validationException, ErrorCodeScenario.TXN_0004, "commonData.txn.ts");
    }

    @ParameterizedTest
    @ValueSource(strings = {"87687\\", "tnf098970\"", "TX\\NGMTS64C8B9H1*8(0240731T224737243", "TXN\"GMTS64C8B9H14800240731T2247372431",
            "\"txnGMTS64C8B9H\\14800240731T2247372431", "\"TNXGMTS64C8B9H14800240731T2247372431"})
    @DisplayName("TransactionValidations: format validations for commonData.txn.refId")
    void testTxnRefIdValidationFailure(String refId) {
        BusinessRegisterRequest businessRequest = getBusinessRequest();
        businessRequest.getCommonData().getTxn().setRefId(refId);
        ValidationException validationException = Assertions.assertThrows(ValidationException.class,
                () -> formatValidator.validateBusiness(businessRequest));
        assertValidationException(validationException, ErrorCodeScenario.INPUT_0002, "commonData.txn.refId");
    }

    @ParameterizedTest
    @ValueSource(strings = {"87687\\", "tnf098970\"", "TX\\NGMTS64C8B9H1*8(0240731T224737243", "TXN\"GMTS64C8B9H14800240731T2247372431",
            "\"txnGMTS64C8B9H\\14800240731T2247372431", "\"TNXGMTS64C8B9H14800240731T2247372431"})
    @DisplayName("TransactionValidations: format validations for commonData.txn.refUrl")
    void testTxnRefUrlValidationFailure(String refUrl) {
        BusinessRegisterRequest businessRequest = getBusinessRequest();
        businessRequest.getCommonData().getTxn().setRefUrl(refUrl);
        ValidationException validationException = Assertions.assertThrows(ValidationException.class,
                () -> formatValidator.validateBusiness(businessRequest));
        assertValidationException(validationException, ErrorCodeScenario.INPUT_0002, "commonData.txn.refUrl");
    }

    @ParameterizedTest
    @ValueSource(strings = {"87687\\", "tnf098970\"", "TX\\NGMTS64C8B9H1*8(0240731T224737243", "TXN\"GMTS64C8B9H14800240731T2247372431",
            "\"txnGMTS64C8B9H\\14800240731T2247372431", "\"TNXGMTS64C8B9H14800240731T2247372431", "ENTITY", "\"ENTITY\"",
            "BUSINESS ENTITY ", "ENTITY BUSINESS", "business entity", "BUSINESS entity"})
    @DisplayName("TransactionValidations: format validations for commonData.txn.type")
    void testTxnTypeValidationFailure(String type) {
        BusinessRegisterRequest businessRequest = getBusinessRequest();
        businessRequest.getCommonData().getTxn().setType(type);
        ValidationException validationException = Assertions.assertThrows(ValidationException.class,
                () -> formatValidator.validateBusiness(businessRequest));
        assertValidationException(validationException, ErrorCodeScenario.INPUT_0002, "commonData.txn.type");
    }

    @ParameterizedTest
    @ValueSource(strings = {"87687\\", "tnf098970\"", "TX\\NGMTS64C8B9H1*8(0240731T224737243", "TXN\"GMTS64C8B9H14800240731T2247372431",
            "\"txnGMTS64C8B9H\\14800240731T2247372431", "\"TNXGMTS64C8B9H14800240731T2247372431", "\"ENTITY\"",
            "BUSINESS ENTITY \\", "ENTITY \"BUSINESS\"", "\"business entity", "BUSINESS \\entity"})
    @DisplayName("TransactionValidations: format validations for commonData.txn.note")
    void testTxnNoteValidationFailure(String note) {
        BusinessRegisterRequest businessRequest = getBusinessRequest();
        businessRequest.getCommonData().getTxn().setNote(note);
        ValidationException validationException = Assertions.assertThrows(ValidationException.class,
                () -> formatValidator.validateBusiness(businessRequest));
        assertValidationException(validationException, ErrorCodeScenario.INPUT_0002, "commonData.txn.note");
    }
}
