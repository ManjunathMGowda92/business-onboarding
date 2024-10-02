package org.fourstack.backoffice.util;

import org.fourstack.backoffice.constants.KeyConstants;

public final class KeyGenerationUtil {
    private KeyGenerationUtil() {
    }

    public static String generateAiEntityKey(String aiId) {
        String aiEntityKey = KeyConstants.AI_ENTITY_KEY;
        return aiEntityKey.replaceAll(KeyConstants.KEY_AI_ID, aiId);
    }


}
