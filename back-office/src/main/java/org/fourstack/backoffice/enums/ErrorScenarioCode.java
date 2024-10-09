package org.fourstack.backoffice.enums;

import lombok.Getter;

@Getter
public enum ErrorScenarioCode {
    BO_AI_0001("AI0001", "No Agent Institute Entities exists in BO"),
    BO_AI_0002("AI0002", "No Agent Institute Entity exist for Id"),
    BO_OU_0001("OU0001", "No Operation Unit Entities exists in BO"),
    BO_OU_0002("OU0002", "No Operation Unit Entity exist for Id"),
    BO_AI_OU_001("AIOU0001", "No Entities exist for AI-OU mappings"),
    BO_AI_OU_002("AIOU0002", "No AI-OU mapping Entity exist for given IDs"),
    BO_AI_OU_003("AIOU0003", "No AI-OU mapping Entity exist for give AiId");

    ErrorScenarioCode(String errorCode, String errorMsg){
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }
    private final String errorCode;
    private final String errorMsg;
}
