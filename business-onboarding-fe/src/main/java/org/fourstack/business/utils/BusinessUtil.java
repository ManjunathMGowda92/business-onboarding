package org.fourstack.business.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.apache.kafka.clients.admin.NewTopic;
import org.fourstack.business.constants.AppConstants;
import org.fourstack.business.constants.LoggerConstants;
import org.fourstack.business.enums.ErrorCodeScenario;
import org.fourstack.business.enums.OperationStatus;
import org.fourstack.business.exceptions.InvalidInputException;
import org.fourstack.business.exceptions.MissingFieldException;
import org.fourstack.business.exceptions.ValidationException;
import org.fourstack.business.model.ValidationResult;
import org.springframework.kafka.config.TopicBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.Objects;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class BusinessUtil {
    private BusinessUtil() {
    }

    private static ObjectMapper objectMapper;
    private static XmlMapper xmlMapper;

    private static ObjectMapper getInstance() {
        if (Objects.isNull(objectMapper)) {
            synchronized (BusinessUtil.class) {
                if (Objects.isNull(objectMapper)) {
                    objectMapper = new ObjectMapper();
                }
            }
        }
        return objectMapper;
    }

    private static XmlMapper getXmlMapper() {
        if (Objects.isNull(xmlMapper)) {
            synchronized (BusinessUtil.class) {
                if (Objects.isNull(xmlMapper)) {
                    xmlMapper = new XmlMapper();
                }
            }
        }
        return xmlMapper;
    }

    public static String convertToString(Object obj) {
        try {
            return getInstance().writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new InvalidInputException("Exception in converting the Object to json string");
        }
    }

    public static <T> T convertToObject(String source, Class<T> classObj) {
        try {
            return getInstance().readValue(source, classObj);
        } catch (JsonProcessingException e) {
            throw new InvalidInputException("Exception in converting the json string to Object");
        }
    }

    public static <T> T deserializeXML(String source, Class<T> classObj) {
        try {
            return getXmlMapper().readValue(source, classObj);
        } catch (JsonProcessingException e) {
            throw new InvalidInputException("Exception in converting the XML string to Object");
        }
    }

    public static String getCurrentTimeStamp() {
        OffsetDateTime dateTime = OffsetDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
        return dateTime.format(formatter);
    }

    public static String generateUniqueId(String prefix) {
        StringBuilder builder = new StringBuilder();
        if (isNotNullOrEmpty(prefix)) {
            builder.append(prefix);
        }
        UUID uuid = UUID.randomUUID();
        builder.append(uuid.toString().replace("-", "").toUpperCase());
        return builder.toString();
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

    public static boolean isNull(Object object) {
        return Objects.isNull(object);
    }

    public static boolean isNotNull(Object object) {
        return Objects.nonNull(object);
    }

    public static boolean isNullOrEmpty(String str) {
        return null == str || str.isBlank();
    }

    public static boolean isNotNullOrEmpty(String str) {
        return null != str && !str.isBlank();
    }

    public static void validateObject(Object object, String fieldName) {
        if (null == object) {
            throw generateMissingFieldException(fieldName, LoggerConstants.NULL_OBJECT);
        }

        if (object instanceof Collection<?> collection && isCollectionNullOrEmpty(collection)) {
            throw generateMissingFieldException(fieldName, LoggerConstants.NULL_OBJECT);
        }

        if (object instanceof CharSequence str && str.isEmpty()) {
            throw generateMissingFieldException(fieldName, LoggerConstants.NULL_OBJECT);
        }
    }

    public static boolean isObjectNullOrEmpty(Object object) {
        if (null == object) {
            return true;
        }

        if (object instanceof Collection<?> collection && isCollectionNullOrEmpty(collection)) {
            return true;
        }

        return object instanceof CharSequence str && str.isEmpty();
    }

    public static ValidationResult generateSuccessValidation() {
        return getValidationResult(null, null, null, OperationStatus.SUCCESS);
    }

    public static ValidationResult generateFailureValidation(String errorCode, String errorMsg, String fieldName) {
        return getValidationResult(errorCode, errorMsg, fieldName, OperationStatus.FAILURE);
    }

    private static ValidationResult getValidationResult(String errorCode, String errorMsg,
                                                        String fieldName, OperationStatus status) {
        return new ValidationResult(status, errorCode, errorMsg, fieldName);
    }

    public static MissingFieldException generateMissingFieldException(String fieldName, String message) {
        return new MissingFieldException(message, fieldName);
    }

    public static ValidationException generateValidationException(String message, String errorCode,
                                                                  String errorMessage, String fieldName) {
        return new ValidationException(message, errorCode, errorMessage, fieldName);
    }

    public static boolean validatePanFormat(String panValue) {
        Pattern pattern = Pattern.compile(AppConstants.PAN_REGEX);
        Matcher matcher = pattern.matcher(panValue);
        return matcher.matches();
    }

    public static boolean validatePanAndBusinessType(String panValue, String businessType) {
        if ("SOLE PROP".equals(businessType)) {
            return panValue.charAt(3) == 'P';
        } else {
            return panValue.charAt(3) != 'P';
        }
    }

    public static long validateTimeStampAndGetDifference(String timestamp, String fieldName) {
        try {
            LocalDateTime dateTime = LocalDateTime.parse(timestamp, DateTimeFormatter.ISO_DATE_TIME);
            LocalDate date = LocalDate.from(dateTime);
            if (!LocalDate.now().isEqual(date)) {
                throw generateValidationException("Mismatch between input and system date", ErrorCodeScenario.TXN_0005.getErrorCode(),
                        ErrorCodeScenario.TXN_0005.getErrorMsg(), fieldName);
            }
            LocalDateTime currentTime = LocalDateTime.now();
            long timeDifference = ChronoUnit.SECONDS.between(currentTime, dateTime);
            return Math.abs(timeDifference);
        } catch (DateTimeParseException e) {
            throw generateValidationException("Invalid Time Format", ErrorCodeScenario.INPUT_0002.getErrorCode(),
                    ErrorCodeScenario.INPUT_0002.getErrorMsg(), fieldName);
        }
    }
}
