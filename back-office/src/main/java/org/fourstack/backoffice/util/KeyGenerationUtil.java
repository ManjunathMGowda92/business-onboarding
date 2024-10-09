package org.fourstack.backoffice.util;

import org.fourstack.backoffice.constants.KeyConstants;

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
}
