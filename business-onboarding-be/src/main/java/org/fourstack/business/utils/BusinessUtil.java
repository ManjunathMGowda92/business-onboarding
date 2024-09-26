package org.fourstack.business.utils;

import org.apache.kafka.clients.admin.NewTopic;
import org.fourstack.business.entity.MainOrgIdEntity;
import org.fourstack.business.enums.ErrorScenarioCode;
import org.fourstack.business.exception.InvalidInputException;
import org.fourstack.business.exception.InvalidTransactionException;
import org.fourstack.business.exception.ValidationException;
import org.fourstack.business.model.TransactionError;
import org.springframework.kafka.config.TopicBuilder;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.Set;

public final class BusinessUtil {
    private BusinessUtil() {
    }

    private static final char[] ALPHABETS = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N',
            'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
    private static final Random random = new Random();

    public static String getCurrentTimeStamp() {
        OffsetDateTime dateTime = OffsetDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
        return dateTime.format(formatter);
    }

    public static long getTimeDifference(Instant instant) {
        Instant currentTime = Instant.now();
        Duration timeDifference = Duration.between(currentTime, instant);
        return Math.abs(timeDifference.getSeconds());
    }

    public static String generateAlphaNumericID(int length, String prefix) {
        StringBuilder builder = new StringBuilder();
        if (isNotNullOrEmpty(prefix)) {
            builder.append(prefix);
        }
        for (int i = 0; i < length; i++) {
            if (i % 2 == 0) {
                builder.append(ALPHABETS[random.nextInt(ALPHABETS.length)]);
            } else {
                builder.append(random.nextInt(9));
            }
        }
        return builder.toString();
    }

    public static boolean isNotNullOrEmpty(String str) {
        return Objects.nonNull(str) && !str.isEmpty() && !str.isBlank();
    }

    public static NewTopic createTopic(String name, int partitions, int replicaCount) {
        return TopicBuilder.name(name)
                .partitions(partitions)
                .replicas(replicaCount)
                .build();
    }

    public static boolean isCollectionNotNullOrEmpty(Collection<?> collection) {
        return collection != null && !collection.isEmpty();
    }

    public static boolean isCollectionNullOrEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

    public static boolean isMapNotNullOrEmpty(Map<?, ?> map) {
        return map != null && !map.isEmpty();
    }

    public static boolean isNotNull(Object object) {
        return Objects.nonNull(object);
    }

    public static boolean isNull(Object object) {
        return Objects.isNull(object);
    }

    public static ValidationException generateValidationException(String message, String fieldName,
                                                                  ErrorScenarioCode scenarioCode) {
        return new ValidationException(message, scenarioCode.getErrorCode(), scenarioCode.getErrorMsg(), fieldName);
    }

    public static Set<String> extractAllB2BIds(MainOrgIdEntity orgIdEntity) {
        Set<String> b2bIds = new HashSet<>();
        if (isCollectionNotNullOrEmpty(orgIdEntity.getPublicB2BIds())) {
            b2bIds.addAll(orgIdEntity.getPublicB2BIds());
        }
        if (isCollectionNotNullOrEmpty(orgIdEntity.getPrivateB2BIds())) {
            b2bIds.addAll(orgIdEntity.getPrivateB2BIds());
        }
        if (isNotNullOrEmpty(orgIdEntity.getDefaultB2BId())) {
            b2bIds.add(orgIdEntity.getDefaultB2BId());
        }
        return b2bIds;
    }

    public static Set<String> extractAllIdentifiers(MainOrgIdEntity orgIdEntity) {
        Set<String> identifiers = new HashSet<>();
        if (isNotNullOrEmpty(orgIdEntity.getPrimaryIdentifier())) {
            identifiers.add(orgIdEntity.getPrimaryIdentifier());
        }
        if (isCollectionNotNullOrEmpty(orgIdEntity.getOtherIdentifiers())) {
            identifiers.addAll(orgIdEntity.getOtherIdentifiers());
        }
        return identifiers;
    }

    public static String getFormattedTimeStamp(String format) {
        try {
            return LocalDateTime.now().format(DateTimeFormatter.ofPattern(format));
        } catch (Exception exception) {
            throw new InvalidInputException("Invalid datetime format given", "", "");
        }
    }

    public static int getIntValue(String value, int defaultValue) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException exception) {
            return defaultValue;
        }
    }

    public static TransactionError convertToTxnError(InvalidTransactionException exception) {
        ErrorScenarioCode scenarioCode = exception.getScenarioCode();
        return generateTxnError(exception.getMessage(), scenarioCode.getErrorCode(),
                scenarioCode.getErrorMsg(), exception.getFieldName());
    }

    public static TransactionError convertToTxnError(ValidationException exception) {
        return generateTxnError(exception.getMessage(), exception.getErrorCode(),
                exception.getErrorMsg(), exception.getErrorField());
    }

    public static TransactionError generateTxnError(String msg, String errorCode, String errorMsg, String errorField) {
        TransactionError txnError = new TransactionError();
        txnError.setErrorMsg(errorMsg);
        txnError.setErrorCode(errorCode);
        txnError.setErrorField(errorField);
        txnError.setMessage(msg);
        txnError.setTimeStamp(getCurrentTimeStamp());
        return txnError;
    }
}
