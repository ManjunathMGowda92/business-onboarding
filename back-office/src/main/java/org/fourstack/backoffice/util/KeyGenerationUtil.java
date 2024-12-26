package org.fourstack.backoffice.util;

import org.fourstack.backoffice.constants.KeyConstants;

import java.time.Instant;

public final class KeyGenerationUtil {
  private KeyGenerationUtil() {
  }

  public static String generateAiEntityKey(String aiId) {
    String aiEntityKey = KeyConstants.AI_ENTITY_KEY;
    return aiEntityKey.replaceAll(KeyConstants.KEY_AI_ID, aiId);
  }


  public static String generateOuEntityKey(String ouId) {
    return KeyConstants.OU_ENTITY_KEY.replaceAll(KeyConstants.KEY_OU_ID, ouId);
  }

  public static String generateAiOuEntityKey(String aiId, String ouId) {
    return KeyConstants.AI_OU_ENTITY_KEY.replaceAll(KeyConstants.KEY_AI_ID, aiId)
            .replaceAll(KeyConstants.KEY_OU_ID, ouId);
  }

  public static String generateKycRequestKey(String objectId, String requestType) {
    return KeyConstants.KYC_REQUEST_KEY.replaceAll(KeyConstants.KEY_OBJECT_ID, objectId)
            .replaceAll(KeyConstants.KEY_REQUEST_TYPE, requestType);
  }

  public static String generateKycAuditKey(String objectId, String requestType) {
    return generateKycRequestKey(objectId, requestType)
            .concat(",").concat("" + Instant.now().getEpochSecond());
  }

  public static String generateKafkaFailureAuditKey(String requestType) {
    return KeyConstants.KYC_FAILURE_AUDIT_KEY.replaceAll(KeyConstants.KEY_REQUEST_TYPE, requestType)
            .concat(",").concat("" + Instant.now().getEpochSecond());
  }

  public static String generateKafkaResponseAuditKey(String objectId, String requestType) {
    return KeyConstants.KYC_RESPONSE_AUDIT_KEY.replaceAll(KeyConstants.KEY_REQUEST_TYPE, requestType)
            .replaceAll(KeyConstants.KEY_OBJECT_ID, objectId)
            .concat(",").concat("" + Instant.now().getEpochSecond());
  }
}
