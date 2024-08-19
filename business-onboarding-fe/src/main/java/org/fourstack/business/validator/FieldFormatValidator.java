package org.fourstack.business.validator;

import org.fourstack.business.constants.AppConstants;
import org.fourstack.business.constants.ValidationConstants;
import org.fourstack.business.enums.ErrorCodeScenario;
import org.fourstack.business.enums.OperationStatus;
import org.fourstack.business.exceptions.MissingFieldException;
import org.fourstack.business.exceptions.ValidationException;
import org.fourstack.business.model.AdditionalInfo;
import org.fourstack.business.model.Address;
import org.fourstack.business.model.B2BId;
import org.fourstack.business.model.B2BIdRegisterRequest;
import org.fourstack.business.model.BankAccount;
import org.fourstack.business.model.BusinessIdentifier;
import org.fourstack.business.model.BusinessRegisterRequest;
import org.fourstack.business.model.CheckBusinessRequest;
import org.fourstack.business.model.CheckInstitute;
import org.fourstack.business.model.CommonData;
import org.fourstack.business.model.ContactNumber;
import org.fourstack.business.model.Institute;
import org.fourstack.business.model.RequesterB2B;
import org.fourstack.business.model.SearchBusinessRequest;
import org.fourstack.business.model.SearchCriteria;
import org.fourstack.business.model.SearchRequest;
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
        ValidationResult result = validateInstitute(request.getInstitute());
        if (BusinessUtil.isNotNull(result) && OperationStatus.FAILURE.equals(result.status())) {
            return result;
        }
        validateAdditionalInfoList(request.getAdditionalInfoList());
        return BusinessUtil.generateSuccessValidation();
    }

    public ValidationResult validateB2BIdRequest(B2BIdRegisterRequest request) {
        validateRequest(request);
        validateCommonRequestData(request.getCommonData());
        validateOnboardingB2BId(request.getOnboardingB2BIds());
        validateB2BIds(request.getRegB2BIds().getIds());
        validateAdditionalInfoList(request.getAdditionalInfoList());
        return BusinessUtil.generateSuccessValidation();
    }

    public ValidationResult validateCheckBusiness(CheckBusinessRequest request) {
        validateRequest(request);
        validateCommonRequestData(request.getCommonData());
        DefaultValidator<CheckInstitute> validator = new DefaultValidator<>();
        CheckInstitute checkInstitute = request.getCheckInstitute();
        validator.validate(ValidationConstants.CHECK_INSTITUTE_VALIDATIONS, checkInstitute);
        ValidationResult result = validateOnboardingPan(checkInstitute.getValue(), checkInstitute.getType(),
                ValidationConstants.CHECK_INSTITUTE_VALUE);
        if (BusinessUtil.isNotNull(result) && OperationStatus.FAILURE.equals(result.status())) {
            return result;
        }
        validateAdditionalInfoList(request.getAdditionalInfoList());
        return BusinessUtil.generateSuccessValidation();
    }

    public ValidationResult validateSearchBusiness(SearchBusinessRequest request) {
        validateRequest(request);
        validateCommonRequestData(request.getCommonData());
        validateOnboardingB2BId(request.getOnboardingB2BIds());
        return validateSearchRequest(request.getSearch());
    }

    private ValidationResult validateSearchRequest(SearchRequest search) {
        for (SearchCriteria criteria : search.getCriteria()) {
            ValidationResult result = validateSearchCriteria(criteria);
            if (BusinessUtil.isNotNull(result) && OperationStatus.FAILURE.equals(result.status())) {
                return result;
            }
        }
        return BusinessUtil.generateSuccessValidation();
    }

    private ValidationResult validateSearchCriteria(SearchCriteria searchCriteria) {
        DefaultValidator<SearchCriteria> validator = new DefaultValidator<>();
        validator.validate(ValidationConstants.SEARCH_CRITERIA_VALIDATIONS, searchCriteria);
        String searchParameter = searchCriteria.getSearchParameter();
        if (AppConstants.PAN.equals(searchParameter)) {
            boolean isValidPan = BusinessUtil.validatePanFormat(searchCriteria.getValue());
            if (!isValidPan) {
                return BusinessUtil.generateFailureValidation(ErrorCodeScenario.BONB_0001.getErrorCode(),
                        ErrorCodeScenario.BONB_0001.getErrorMsg(), ValidationConstants.SEARCH_CRITERIA_VALUE);
            }
        }
        return BusinessUtil.generateSuccessValidation();
    }

    private void validateRequest(SearchBusinessRequest request) {
        validateCommonData(request.getCommonData());
        BusinessUtil.validateObject(request.getOnboardingB2BIds(), ValidationConstants.ONBOARDING_B2B_IDS);
        BusinessUtil.validateObject(request.getSearch(), ValidationConstants.SEARCH);
        BusinessUtil.validateObject(request.getSearch().getCriteria(), ValidationConstants.SEARCH_CRITERIA);
    }

    private ValidationResult validateOnboardingPan(String panValue, String businessType, String fieldName) {
        boolean isValidPan = BusinessUtil.validatePanFormat(panValue);
        if (!isValidPan) {
            return BusinessUtil.generateFailureValidation(ErrorCodeScenario.BONB_0001.getErrorCode(),
                    ErrorCodeScenario.BONB_0001.getErrorMsg(), fieldName);
        }
        boolean inValidPanCombo = BusinessUtil.validatePanAndBusinessType(panValue, businessType);
        if (inValidPanCombo) {
            return BusinessUtil.generateFailureValidation(ErrorCodeScenario.BONB_0002.getErrorCode(),
                    ErrorCodeScenario.BONB_0002.getErrorMsg(), fieldName);
        }
        return BusinessUtil.generateSuccessValidation();
    }

    private void validateRequest(CheckBusinessRequest request) {
        validateCommonData(request.getCommonData());
        BusinessUtil.validateObject(request.getCheckInstitute(), ValidationConstants.CHECK_INSTITUTE);
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

    private void validateOnboardingB2BId(RequesterB2B onboardingB2BIds) {
        DefaultValidator<RequesterB2B> validator = new DefaultValidator<>();
        validator.validate(ValidationConstants.REQUESTER_B2B_VALIDATIONS, onboardingB2BIds);
    }

    private void validateRequest(B2BIdRegisterRequest request) {
        validateCommonData(request.getCommonData());
        BusinessUtil.validateObject(request.getOnboardingB2BIds(), ValidationConstants.ONBOARDING_B2B_IDS);
        BusinessUtil.validateObject(request.getRegB2BIds(), ValidationConstants.REG_B2B_IDS);
        BusinessUtil.validateObject(request.getRegB2BIds().getIds(), ValidationConstants.REG_B2BIDS_IDS);
    }

    private void validateRequest(BusinessRegisterRequest request) {
        validateCommonData(request.getCommonData());
        BusinessUtil.validateObject(request.getInstitute(), ValidationConstants.INSTITUTE);
    }

    private void validateCommonData(CommonData commonData) {
        BusinessUtil.validateObject(commonData, ValidationConstants.COMMON_DATA);
        BusinessUtil.validateObject(commonData.getHead(), ValidationConstants.COMMON_DATA_HEAD);
        BusinessUtil.validateObject(commonData.getTxn(), ValidationConstants.COMMON_DATA_TXN);
        BusinessUtil.validateObject(commonData.getDevice(), ValidationConstants.COMMON_DATA_DEVICE);
        BusinessUtil.validateObject(commonData.getDevice().getTag(), ValidationConstants.COMMON_DATA_DEVICE_TAG);
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

    private ValidationResult validateInstitute(Institute institute) {
        DefaultValidator<Institute> validator = new DefaultValidator<>();
        BusinessUtil.validateObject(institute.getLei(), "institute.lei");
        validator.validate(ValidationConstants.INSTITUTE_VALIDATIONS, institute);
        ValidationResult result = validateOnboardingPan(institute.getLei().getValue(), institute.getLei().getType(),
                ValidationConstants.INSTITUTE_LIE_VALUE);
        if (BusinessUtil.isNotNull(result) && OperationStatus.FAILURE.equals(result.status())) {
            return result;
        }
        BusinessUtil.validateObject(institute.getPrimaryIdentifier(), ValidationConstants.INSTITUTE_PRIMARY_IDENTIFIER);
        validateIdentifier(institute.getPrimaryIdentifier(), "institute.primaryIdentifier");
        validateOtherIdentifiers(institute.getOtherIdentifiers());
        validateAddressList(institute.getAddresses());
        validateBankAccounts(institute.getBankAccounts());
        validateContactNumber(institute.getPrimaryContact());
        BusinessUtil.validateObject(institute.getPrimaryContact(), ValidationConstants.INSTITUTE_PRIMARY_CONTACT);
        validateContactNumbers(institute.getContactNumbers());
        validateEmail(institute.getPrimaryEmail(), true, ValidationConstants.INSTITUTE_PRIMARY_EMAIL);
        validateEmails(institute.getEmails());
        return BusinessUtil.generateSuccessValidation();
    }

    private void validateEmails(List<String> emails) {
        if (BusinessUtil.isCollectionNotNullOrEmpty(emails)) {
            emails.forEach(email -> validateEmail(email, false, ValidationConstants.INSTITUTE_EMAILS));
        }
    }

    private void validateEmail(String email, boolean isRequired, String fieldName) {
        if (isRequired && BusinessUtil.isNullOrEmpty(email)) {
            throw BusinessUtil.generateMissingFieldException(fieldName, "Email field is missing");
        }
        Pattern pattern = Pattern.compile(ValidationConstants.EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        if (!matcher.matches()) {
            throw new ValidationException("Email is not matched with pattern", ErrorCodeScenario.INPUT_0002.getErrorCode(),
                    ErrorCodeScenario.INPUT_0002.getErrorMsg(), fieldName);
        }
    }

    private void validateContactNumbers(List<ContactNumber> contactNumbers) {
        if (BusinessUtil.isCollectionNotNullOrEmpty(contactNumbers)) {
            contactNumbers.forEach(contactNumber -> {
                BusinessUtil.validateObject(contactNumber, ValidationConstants.INSTITUTE_CONTACT_NUMBERS);
                validateContactNumber(contactNumber);
            });
        }
    }

    private void validateContactNumber(ContactNumber contact) {
        DefaultValidator<ContactNumber> validator = new DefaultValidator<>();
        validator.validate(ValidationConstants.CONTACT_VALIDATIONS, contact);
    }

    private void validateBankAccounts(List<BankAccount> bankAccounts) {
        BusinessUtil.validateObject(bankAccounts, ValidationConstants.INSTITUTE_BANK_ACCOUNTS);
        bankAccounts.forEach(account -> {
            BusinessUtil.validateObject(account, ValidationConstants.INSTITUTE_BANK_ACCOUNTS);
            validateBankAccount(account);
        });
    }

    private void validateBankAccount(BankAccount account) {
        DefaultValidator<BankAccount> validator = new DefaultValidator<>();
        validator.validate(ValidationConstants.BANK_ACCOUNT_VALIDATIONS, account);
    }

    private void validateAddressList(List<Address> addresses) {
        BusinessUtil.validateObject(addresses, ValidationConstants.INSTITUTE_ADDRESSES);
        addresses.forEach(address -> {
            BusinessUtil.validateObject(address, ValidationConstants.INSTITUTE_ADDRESSES);
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
                BusinessUtil.validateObject(identifier, ValidationConstants.INSTITUTE_OTHER_IDENTIFIERS);
                validateIdentifier(identifier, "institute.otherIdentifiers");
            });
        }
    }

    private void validateIdentifier(BusinessIdentifier primaryIdentifier, String parentObjectName) {
        try {
            DefaultValidator<BusinessIdentifier> validator = new DefaultValidator<>();
            validator.validate(ValidationConstants.BUSINESS_IDENTIFIER_VALIDATIONS, primaryIdentifier);
        } catch (MissingFieldException exception) {
            throw new MissingFieldException(exception.getMessage(),
                    parentObjectName.concat(".").concat(exception.getFieldName()));
        } catch (ValidationException exception) {
            throw new ValidationException(exception.getMessage(), exception.getErrorCode(), exception.getErrorMessage(),
                    parentObjectName.concat(".").concat(exception.getFieldName()));
        }
    }

    private void validateCommonRequestData(CommonData commonData) {
        DefaultValidator<Object> defaultValidator = new DefaultValidator<>();
        defaultValidator.validate(ValidationConstants.HEADER_VALIDATIONS, commonData.getHead());
        defaultValidator.validate(ValidationConstants.TRANSACTION_VALIDATIONS, commonData.getTxn());
        defaultValidator.validate(ValidationConstants.DEVICE_VALIDATIONS, commonData.getDevice());
    }
}
