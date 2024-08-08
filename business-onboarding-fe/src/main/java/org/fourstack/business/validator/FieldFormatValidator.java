package org.fourstack.business.validator;

import org.fourstack.business.constants.ValidationConstants;
import org.fourstack.business.exceptions.ValidationException;
import org.fourstack.business.model.AdditionalInfo;
import org.fourstack.business.model.Address;
import org.fourstack.business.model.B2BId;
import org.fourstack.business.model.B2BIdRegisterRequest;
import org.fourstack.business.model.BankAccount;
import org.fourstack.business.model.BusinessIdentifier;
import org.fourstack.business.model.BusinessRegisterRequest;
import org.fourstack.business.model.CommonData;
import org.fourstack.business.model.ContactNumber;
import org.fourstack.business.model.Institute;
import org.fourstack.business.model.RequesterB2BId;
import org.fourstack.business.model.ValidationResult;
import org.fourstack.business.utils.BusinessUtil;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class FieldFormatValidator {

    public ValidationResult validateBusiness(BusinessRegisterRequest request) {
        validateRequest(request);
        validateCommonRequestData(request.getCommonData());
        validateInstitute(request.getInstitute());
        validateAdditionalInfoList(request.getAdditionalInfoList());
        return BusinessUtil.generateSuccessValidation();
    }

    public ValidationResult validateB2BIdRequest(B2BIdRegisterRequest request) {
        validateRequest(request);
        validateCommonRequestData(request.getCommonData());
        validateOnboardingB2BId(request.getOnboardingB2BIds());
        validateB2BIds(request.getRegB2BIds().getIds());
        return BusinessUtil.generateSuccessValidation();
    }

    private void validateB2BIds(List<B2BId> ids) {
        if (BusinessUtil.isCollectionNotNullOrEmpty(ids)) {
            ids.forEach(this::validateB2BId);
        }
    }

    private void validateB2BId(B2BId b2BId) {
        DefaultValidator<B2BId> validator = new DefaultValidator<>();
        validator.validate(ValidationConstants.B2B_ID_VALIDATIONS, b2BId);
    }

    private void validateOnboardingB2BId(RequesterB2BId onboardingB2BIds) {
        DefaultValidator<RequesterB2BId> validator = new DefaultValidator<>();
        validator.validate(ValidationConstants.REQUESTER_B2B_VALIDATIONS, onboardingB2BIds);
    }

    private void validateRequest(B2BIdRegisterRequest request) {
        validateCommonData(request.getCommonData());
        BusinessUtil.validateObject(request.getOnboardingB2BIds(), "onboardingB2BIds");
        BusinessUtil.validateObject(request.getRegB2BIds(), "regB2BIds");
        BusinessUtil.validateObject(request.getRegB2BIds().getIds(), "regB2BIds.ids");
    }

    private void validateRequest(BusinessRegisterRequest request) {
        validateCommonData(request.getCommonData());
        BusinessUtil.validateObject(request.getInstitute(), "institute");
    }

    private void validateCommonData(CommonData commonData) {
        BusinessUtil.validateObject(commonData, "commonData");
        BusinessUtil.validateObject(commonData.getHead(), "commonData.head");
        BusinessUtil.validateObject(commonData.getTxn(), "commonData.txn");
        BusinessUtil.validateObject(commonData.getDevice(), "commonData.device");
        BusinessUtil.validateObject(commonData.getDevice().getTag(), "commonData.device.tag");
    }

    private void validateAdditionalInfoList(List<AdditionalInfo> additionalInfoList) {
        if (BusinessUtil.isCollectionNotNullOrEmpty(additionalInfoList)) {
            additionalInfoList.forEach(this::validateAdditionalInfo);
        }
    }

    private void validateAdditionalInfo(AdditionalInfo additionalInfo) {
        DefaultValidator<AdditionalInfo> validator = new DefaultValidator<>();
        validator.validate(ValidationConstants.ADDITIONAL_INFO_VALIDATIONS, additionalInfo);
    }

    private void validateInstitute(Institute institute) {
        DefaultValidator<Institute> validator = new DefaultValidator<>();
        validator.validate(ValidationConstants.INSTITUTE_VALIDATIONS, institute);
        BusinessUtil.validateObject(institute.getPrimaryIdentifier(), "institute.primaryIdentifier");
        validateIdentifier(institute.getPrimaryIdentifier());
        validateOtherIdentifiers(institute.getOtherIdentifiers());
        validateAddressList(institute.getAddresses());
        validateBankAccounts(institute.getBankAccounts());
        validateContactNumber(institute.getPrimaryContact());
        BusinessUtil.validateObject(institute.getPrimaryContact(), "institute.primaryContact");
        validateContactNumbers(institute.getContactNumbers());
        validateEmail(institute.getPrimaryEmail(), true, "institute.primaryEmail");
        validateEmails(institute.getEmails());
    }

    private void validateEmails(List<String> emails) {
        if (BusinessUtil.isCollectionNotNullOrEmpty(emails)) {
            emails.forEach(email -> validateEmail(email, false, "institute.emails"));
        }
    }

    private void validateEmail(String email, boolean isRequired, String fieldName) {
        if (isRequired && BusinessUtil.isNullOrEmpty(email)) {
            throw BusinessUtil.generateMissingFieldException(fieldName, "Email field is missing");
        }
        Pattern pattern = Pattern.compile(ValidationConstants.EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        if (!matcher.matches()) {
            throw new ValidationException("Email is not matched with pattern", "INP0002", "Invalid field format", fieldName);
        }
    }

    private void validateContactNumbers(List<ContactNumber> contactNumbers) {
        if (BusinessUtil.isCollectionNotNullOrEmpty(contactNumbers)) {
            contactNumbers.forEach(contactNumber -> {
                BusinessUtil.validateObject(contactNumber, "institute.contactNumbers");
                validateContactNumber(contactNumber);
            });
        }
    }

    private void validateContactNumber(ContactNumber contact) {
        DefaultValidator<ContactNumber> validator = new DefaultValidator<>();
        validator.validate(ValidationConstants.CONTACT_VALIDATIONS, contact);
    }

    private void validateBankAccounts(List<BankAccount> bankAccounts) {
        BusinessUtil.validateObject(bankAccounts, "institute.bankAccounts");
        bankAccounts.forEach(account -> {
            BusinessUtil.validateObject(account, "institute.bankAccounts");
            validateBankAccount(account);
        });
    }

    private void validateBankAccount(BankAccount account) {
        DefaultValidator<BankAccount> validator = new DefaultValidator<>();
        validator.validate(ValidationConstants.BANK_ACCOUNT_VALIDATIONS, account);
    }

    private void validateAddressList(List<Address> addresses) {
        BusinessUtil.validateObject(addresses, "institute.addresses");
        addresses.forEach(address -> {
            BusinessUtil.validateObject(address, "institute.addresses");
            validateAddress(address);
        });
    }

    private void validateAddress(Address address) {
        DefaultValidator<Address> validator = new DefaultValidator<>();
        validator.validate(ValidationConstants.ADDRESS_VALIDATIONS, address);
    }

    private void validateOtherIdentifiers(List<BusinessIdentifier> otherIdentifiers) {
        if (BusinessUtil.isCollectionNotNullOrEmpty(otherIdentifiers)) {
            otherIdentifiers.forEach(identifier -> {
                BusinessUtil.validateObject(identifier, "institute.otherIdentifiers");
                validateIdentifier(identifier);
            });
        }
    }

    private void validateIdentifier(BusinessIdentifier primaryIdentifier) {
        DefaultValidator<BusinessIdentifier> validator = new DefaultValidator<>();
        validator.validate(ValidationConstants.BUSINESS_IDENTIFIER_VALIDATIONS, primaryIdentifier);
    }

    private void validateCommonRequestData(CommonData commonData) {
        DefaultValidator<Object> defaultValidator = new DefaultValidator<>();
        defaultValidator.validate(ValidationConstants.HEADER_VALIDATIONS, commonData.getHead());
        defaultValidator.validate(ValidationConstants.TRANSACTION_VALIDATIONS, commonData.getTxn());
        defaultValidator.validate(ValidationConstants.DEVICE_VALIDATIONS, commonData.getDevice());
    }
}
