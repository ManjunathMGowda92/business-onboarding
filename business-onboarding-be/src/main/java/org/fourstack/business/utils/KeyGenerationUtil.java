package org.fourstack.business.utils;

import org.fourstack.business.constants.BusinessConstants;
import org.fourstack.business.constants.DatabaseKeyConstants;

public final class KeyGenerationUtil {
    private KeyGenerationUtil() {
    }

    public static String generateAiEntityKey(String aiId) {
        String aiEntityKey = DatabaseKeyConstants.AI_ENTITY_KEY;
        return aiEntityKey.replaceAll(DatabaseKeyConstants.KEY_AI_ID, aiId);
    }

    public static String generateAuditAiEntityKey(String aiId) {
        String aiEntityKey = DatabaseKeyConstants.AUDIT_AI_ENTITY_KEY;
        return aiEntityKey.replaceAll(DatabaseKeyConstants.KEY_AI_ID, aiId)
                .replaceAll(DatabaseKeyConstants.TIMESTAMP, getAuditTimeStamp());
    }

    public static String generateOuEntityKey(String ouId) {
        String ouEntityKey = DatabaseKeyConstants.OU_ENTITY_KEY;
        return ouEntityKey.replaceAll(DatabaseKeyConstants.KEY_OU_ID, ouId);
    }

    public static String generateAuditOuEntityKey(String ouId) {
        String ouEntityKey = DatabaseKeyConstants.AUDIT_OU_ENTITY_KEY;
        return ouEntityKey.replaceAll(DatabaseKeyConstants.KEY_OU_ID, ouId)
                .replaceAll(DatabaseKeyConstants.TIMESTAMP, getAuditTimeStamp());
    }

    public static String generateAiOuMapEntityKey(String aiId, String ouId) {
        String aiOuMapEntityKey = DatabaseKeyConstants.AI_OU_MAP_ENTITY_KEY;
        return aiOuMapEntityKey.replaceAll(DatabaseKeyConstants.KEY_AI_ID, aiId)
                .replaceAll(DatabaseKeyConstants.KEY_OU_ID, ouId);
    }

    public static String generateAuditAiOuMapEntityKey(String aiId, String ouId) {
        String aiOuMapEntityKey = DatabaseKeyConstants.AUDIT_AI_OU_MAP_ENTITY_KEY;
        return aiOuMapEntityKey.replaceAll(DatabaseKeyConstants.KEY_AI_ID, aiId)
                .replaceAll(DatabaseKeyConstants.KEY_OU_ID, ouId)
                .replaceAll(DatabaseKeyConstants.TIMESTAMP, getAuditTimeStamp());
    }

    public static String generateTransactionEntityKey(String transactionId) {
        String transactionEntityKey = DatabaseKeyConstants.TRANSACTION_ENTITY_KEY;
        return transactionEntityKey.replaceAll(DatabaseKeyConstants.KEY_TXN_ID, transactionId);
    }

    public static String generateTxnAuditEntityKey(String transactionId, String flowType) {
        String auditTransactionEntityKey = DatabaseKeyConstants.AUDIT_TRANSACTION_ENTITY_KEY;
        return auditTransactionEntityKey.replaceAll(DatabaseKeyConstants.KEY_TXN_ID, transactionId)
                .replaceAll(DatabaseKeyConstants.FLOW_TYPE, flowType)
                .replaceAll(DatabaseKeyConstants.TIMESTAMP, getAuditTimeStamp());
    }

    public static String generateBusinessEntityKey(String leiValue, String orgId) {
        String businessEntityKey = DatabaseKeyConstants.BUSINESS_ENTITY_KEY;
        return businessEntityKey.replaceAll(DatabaseKeyConstants.KEY_LEI, leiValue)
                .replaceAll(DatabaseKeyConstants.KEY_BUSINESS_UNIT, orgId);
    }

    public static String generateOrgIdEntityKey(String orgId) {
        String orgIdEntityKey = DatabaseKeyConstants.ORG_ID_ENTITY_KEY;
        return orgIdEntityKey.replaceAll(DatabaseKeyConstants.KEY_BUSINESS_UNIT, orgId);
    }

    public static String generateAiOrgEntityKey(String aiId, String orgId) {
        String aiOrgIdEntityKey = DatabaseKeyConstants.AI_ORG_ID_ENTITY_KEY;
        return aiOrgIdEntityKey.replaceAll(DatabaseKeyConstants.KEY_AI_ID, aiId)
                .replaceAll(DatabaseKeyConstants.KEY_BUSINESS_UNIT, orgId);
    }

    public static String generateBusinessTransactionKey(String leiValue, String orgId, String txnId) {
        String businessTxnEntityKey = DatabaseKeyConstants.BUSINESS_TXN_ENTITY_KEY;
        return businessTxnEntityKey.replaceAll(DatabaseKeyConstants.KEY_TXN_ID, txnId)
                .replaceAll(DatabaseKeyConstants.KEY_LEI, leiValue)
                .replaceAll(DatabaseKeyConstants.KEY_BUSINESS_UNIT, orgId);
    }

    public static String generateOrgIdTransactionKey(String orgId, String txnId) {
        String orgIdTxnEntityKey = DatabaseKeyConstants.ORG_ID_TXN_ENTITY_KEY;
        return orgIdTxnEntityKey.replaceAll(DatabaseKeyConstants.KEY_BUSINESS_UNIT, orgId)
                .replaceAll(DatabaseKeyConstants.KEY_TXN_ID, txnId);
    }

    public static String generateBusinessIdentifierKey(String identifierType, String identifierValue) {
        String businessIdentifierKey = DatabaseKeyConstants.BUSINESS_IDENTIFIER_KEY;
        return businessIdentifierKey.replaceAll(DatabaseKeyConstants.KEY_IDENTIFIER_TYPE, identifierType)
                .replaceAll(DatabaseKeyConstants.KEY_IDENTIFIER_VALUE, identifierValue);
    }

    public static String generateB2BIdentifierKey(String b2bId) {
        String b2bIdentifierKey = DatabaseKeyConstants.B2B_IDENTIFIER_KEY;
        return b2bIdentifierKey.replaceAll(DatabaseKeyConstants.KEY_B2B_ID, b2bId);
    }

    public static String generateKycTransactionKey(String txnId, String orgId) {
        String kycTransactionKey = DatabaseKeyConstants.KYC_TRANSACTION_KEY;
        return kycTransactionKey.replaceAll(DatabaseKeyConstants.KEY_TXN_ID, txnId)
                .replaceAll(DatabaseKeyConstants.KEY_BUSINESS_UNIT, orgId);
    }

    public static String generateKycOrgAiMappingKey(String aiId, String orgId) {
        String kycOrgAiMappingKey = DatabaseKeyConstants.KYC_ORG_AI_MAPPING_KEY;
        return kycOrgAiMappingKey.replaceAll(DatabaseKeyConstants.KEY_AI_ID, aiId)
                .replaceAll(DatabaseKeyConstants.KEY_BUSINESS_UNIT, orgId);
    }

    private static String getAuditTimeStamp() {
        return BusinessUtil.getFormattedTimeStamp(BusinessConstants.FORMAT_YYYY_MM_DD_HH_MM_SS_SSS);
    }
}
