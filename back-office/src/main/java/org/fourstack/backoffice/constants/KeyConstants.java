package org.fourstack.backoffice.constants;

public final class KeyConstants {


  private KeyConstants() {
    }


  public static final String AI_ENTITY_KEY = "MASTER_DATA,AI,<AI_ID>";
  public static final String KEY_AI_ID = "<AI_ID>";
  public static final String OU_ENTITY_KEY = "MASTER_DATA,OU,<OU_ID>";
  public static final String KEY_OU_ID = "<OU_ID>";
  public static final String AI_OU_ENTITY_KEY = "MASTER_DATA,AI,<AI_ID>,OU,<OU_ID>";
  public static final String KYC_REQUEST_KEY = "KYC,<OBJECT_ID>,TYPE,<REQUEST_TYPE>";
  public static final String KEY_OBJECT_ID = "<OBJECT_ID>";
  public static final String KEY_REQUEST_TYPE = "<REQUEST_TYPE>";
  public static final String KYC_FAILURE_AUDIT_KEY = "KYC,RESPONSE,<REQUEST_TYPE>";
  public static final String KYC_RESPONSE_AUDIT_KEY = "KYC,RESPONSE,<OBJECT_ID>,<REQUEST_TYPE>";
}
