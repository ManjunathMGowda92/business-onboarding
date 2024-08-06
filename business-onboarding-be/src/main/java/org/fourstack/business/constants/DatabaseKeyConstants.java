package org.fourstack.business.constants;

public final class DatabaseKeyConstants {
    private DatabaseKeyConstants(){
    }

    public static final String AI_ENTITY_KEY = "BUSINESS,MASTER,V1,AI_ID,<AI_ID>";
    public static final String AUDIT_AI_ENTITY_KEY = "BUSINESS,MASTER,V1,AUDIT,AI_ID,<AI_ID>,TIME,<TIMESTAMP>";
    public static final String OU_ENTITY_KEY = "BUSINESS,MASTER,V1,OU_ID,<OU_ID>";
    public static final String AUDIT_OU_ENTITY_KEY = "BUSINESS,MASTER,V1,AUDIT,OU_ID,<OU_ID>,TIME,<TIMESTAMP>";
    public static final String AI_OU_MAP_ENTITY_KEY = "BUSINESS,MASTER,V1,AI_ID,<AI_ID>,OU,<OU_ID>";
    public static final String AUDIT_AI_OU_MAP_ENTITY_KEY = "BUSINESS,MASTER,V1,AI_ID,<AI_ID>,OU,<OU_ID>,TIME,<TIMESTAMP>";
    public static final String TRANSACTION_ENTITY_KEY = "BUSINESS,MASTER,V1,TXN,<TXN_ID>";
    public static final String AUDIT_TRANSACTION_ENTITY_KEY = "BUSINESS,MASTER,V1,AUDIT,TXN,<TXN_ID>,FLOW,<FLOW_TYPE>,TIME,<TIMESTAMP>";
    public static final String BUSINESS_ENTITY_KEY = "BUSINESS,V1,LEI,<LEI>,BU_ID,<BU_ID>";
    public static final String BUSINESS_TXN_ENTITY_KEY = "BUSINESS,V1,TXN,<TXN_ID>,LEI,<LEI>,BU_ID,<BU_ID>";
    public static final String ORG_ID_ENTITY_KEY = "BUSINESS,V1,BU_ID,<BU_ID>";
    public static final String ORG_ID_TXN_ENTITY_KEY = "BUSINESS,V1,TXN,<TXN_ID>,BU_ID,<BU_ID>";
    public static final String BUSINESS_IDENTIFIER_KEY = "BUSINESS,V1,IDENTIFIER_TYPE,<IDENTIFIER_TYPE>,IDENTIFIER_VALUE,<IDENTIFIER_VALUE>";
    public static final String B2B_IDENTIFIER_KEY = "BUSINESS,V1,BUSINESS_ID,<B2B_ID>";
    public static final String KYC_TRANSACTION_KEY = "BUSINESS,V1,KYC,TXN,<TXN_ID>,BU_ID,<BU_ID>";
    public static final String KYC_ORG_AI_MAPPING_KEY = "BUSINESS,V1,KYC,AI,<AI_ID>,BU_ID,<BU_ID>";
    public static final String KEY_AI_ID = "<AI_ID>";
    public static final String KEY_OU_ID = "<OU_ID>";
    public static final String KEY_LEI = "<LEI>";
    public static final String KEY_BUSINESS_UNIT = "<BU_ID>";
    public static final String KEY_TXN_ID = "<TXN_ID>";
    public static final String KEY_IDENTIFIER_TYPE = "<IDENTIFIER_TYPE>";
    public static final String KEY_IDENTIFIER_VALUE = "<IDENTIFIER_VALUE>";
    public static final String KEY_B2B_ID = "<B2B_ID>";
    public static final String FLOW_TYPE = "<FLOW_TYPE>";
    public static final String TIMESTAMP = "<TIMESTAMP>";
}
