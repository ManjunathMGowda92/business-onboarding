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

class TransactionMandatoryValidationTest extends BaseTest {

    @Test
    @DisplayName("TxnMandatoryValidations: Mandatory validation for commonData.txn")
    void testTxnFailure() {
        BusinessRegisterRequest businessRequest = getBusinessRequest();
        businessRequest.getCommonData().setTxn(null);
        MissingFieldException exception = Assertions.assertThrows(MissingFieldException.class,
                () -> formatValidator.validateBusiness(businessRequest));
        assertMissingFieldException(exception, "commonData.txn");
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {""})
    @DisplayName("TxnMandatoryValidations: Mandatory validation for commonData.txn.id")
    void testTxnIdFailure(String txnId) {
        BusinessRegisterRequest businessRequest = getBusinessRequest();
        businessRequest.getCommonData().getTxn().setId(txnId);
        MissingFieldException exception = Assertions.assertThrows(MissingFieldException.class,
                () -> formatValidator.validateBusiness(businessRequest));
        assertMissingFieldException(exception, "commonData.txn.id");
    }

    /*@ParameterizedTest
    @NullSource
    @ValueSource(strings = {""})
    @DisplayName("TxnMandatoryValidations: Mandatory validation for commonData.txn.ts")
    void testTxnTimeStampFailure(String ts) {
        BusinessRegisterRequest businessRequest = getBusinessRequest();
        businessRequest.getCommonData().getTxn().setTs(ts);
        MissingFieldException exception = Assertions.assertThrows(MissingFieldException.class,
                () -> formatValidator.validateBusiness(businessRequest));
        assertMissingFieldException(exception, "commonData.txn.ts");
    }*/

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {""})
    @DisplayName("TxnMandatoryValidations: Mandatory validation for commonData.txn.type")
    void testTxnTypeFailure(String type) {
        BusinessRegisterRequest businessRequest = getBusinessRequest();
        businessRequest.getCommonData().getTxn().setType(type);
        MissingFieldException exception = Assertions.assertThrows(MissingFieldException.class,
                () -> formatValidator.validateBusiness(businessRequest));
        assertMissingFieldException(exception, "commonData.txn.type");
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {""})
    @DisplayName("TxnMandatoryValidations: success validation for commonData.txn.refId")
    void testSuccessValidationForTxnRefIdNullOrEmpty(String refId) {
        BusinessRegisterRequest businessRequest = getBusinessRequest();
        businessRequest.getCommonData().getTxn().setRefId(refId);
        ValidationResult result = formatValidator.validateBusiness(businessRequest);
        assertSuccessValidation(result);
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {""})
    @DisplayName("TxnMandatoryValidations: success validation for commonData.txn.refUrl")
    void testSuccessValidationForTxnRefUrlNullOrEmpty(String refUrl) {
        BusinessRegisterRequest businessRequest = getBusinessRequest();
        businessRequest.getCommonData().getTxn().setRefUrl(refUrl);
        ValidationResult result = formatValidator.validateBusiness(businessRequest);
        assertSuccessValidation(result);
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {""})
    @DisplayName("TxnMandatoryValidations: success validation for commonData.txn.note")
    void testSuccessValidationForTxnNoteNullOrEmpty(String note) {
        BusinessRegisterRequest businessRequest = getBusinessRequest();
        businessRequest.getCommonData().getTxn().setNote(note);
        ValidationResult result = formatValidator.validateBusiness(businessRequest);
        assertSuccessValidation(result);
    }
}
