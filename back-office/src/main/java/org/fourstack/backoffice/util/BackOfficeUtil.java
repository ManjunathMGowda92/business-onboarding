package org.fourstack.backoffice.util;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;

public final class BackOfficeUtil {
    /**
     * Default Private Constructor
     */
    private BackOfficeUtil(){
    }
    public static String getCurrentTimeStamp() {
        OffsetDateTime dateTime = OffsetDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
        return dateTime.format(formatter);
    }

    public static boolean isCollectionNotNullOrEmpty(Collection<?> collection) {
        return collection != null && !collection.isEmpty();
    }
}
