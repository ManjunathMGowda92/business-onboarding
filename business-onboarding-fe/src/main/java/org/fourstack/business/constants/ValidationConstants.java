package org.fourstack.business.constants;

public final class ValidationConstants {

    private ValidationConstants(){
    }

    public static final String EMAIL_PATTERN = "^[a-zA-Z0-9._%±]+@[a-zA-Z0-9.-]+.[a-zA-Z]{2,}$";
    public static final String HEADER_VALIDATIONS= "HEADER_VALIDATIONS";
    public static final String TRANSACTION_VALIDATIONS= "TRANSACTION_VALIDATIONS";
    public static final String DEVICE_VALIDATIONS= "DEVICE_VALIDATIONS";
    public static final String INSTITUTE_VALIDATIONS= "INSTITUTE_VALIDATIONS";
    public static final String BUSINESS_IDENTIFIER_VALIDATIONS= "BUSINESS_IDENTIFIER_VALIDATIONS";
    public static final String ADDRESS_VALIDATIONS= "ADDRESS_VALIDATIONS";
    public static final String BANK_ACCOUNT_VALIDATIONS= "BANK_ACCOUNT_VALIDATIONS";
    public static final String CONTACT_VALIDATIONS= "CONTACT_VALIDATIONS";
    public static final String ADDITIONAL_INFO_VALIDATIONS= "ADDITIONAL_INFO_VALIDATIONS";
    public static final String REQUESTER_B2B_VALIDATIONS= "REQUESTER_B2B_VALIDATIONS";
    public static final String B2B_ID_VALIDATIONS= "B2B_ID_VALIDATIONS";
    public static final String CHECK_INSTITUTE_VALIDATIONS= "CHECK_INSTITUTE_VALIDATIONS";
    public static final String SEARCH_CRITERIA_VALIDATIONS = "SEARCH_CRITERIA_VALIDATIONS";
    public static final String SEARCH_CRITERIA_VALUE = "search.criteria.value";
    public static final String SEARCH = "search";
    public static final String SEARCH_CRITERIA = "search.criteria";

    public static final String INSTITUTE_PRIMARY_IDENTIFIER = "institute.primaryIdentifier";
    public static final String INSTITUTE_PRIMARY_CONTACT = "institute.primaryContact";
    public static final String INSTITUTE_PRIMARY_EMAIL = "institute.primaryEmail";
    public static final String INSTITUTE_EMAILS = "institute.emails";
    public static final String INSTITUTE_LIE_VALUE = "institute.lei.value";
    public static final String INSTITUTE_CONTACT_NUMBERS = "institute.contactNumbers";
    public static final String INSTITUTE_BANK_ACCOUNTS = "institute.bankAccounts";
    public static final String INSTITUTE_ADDRESSES = "institute.addresses";
    public static final String INSTITUTE_OTHER_IDENTIFIERS = "institute.otherIdentifiers";
    public static final String COMMON_DATA_HEAD = "commonData.head";
    public static final String HEAD_TS = "commonData.head.ts";
    public static final String COMMON_DATA_TXN = "commonData.txn";
    public static final String TXN_TS = "commonData.txn.ts";
    public static final String COMMON_DATA_DEVICE = "commonData.device";
    public static final String COMMON_DATA_DEVICE_TAG = "commonData.device.tag";
    public static final String COMMON_DATA = "commonData";
    public static final String INSTITUTE = "institute";
    public static final String ONBOARDING_B2B_IDS = "onboardingB2BIds";
    public static final String REG_B2B_IDS = "regB2BIds";
    public static final String REG_B2BIDS_IDS = "regB2BIds.ids";
    public static final String CHECK_INSTITUTE = "checkInstitute";
    public static final String CHECK_INSTITUTE_VALUE = "checkInstitute.value";
}
