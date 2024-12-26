package org.fourstack.backoffice.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.admin.NewTopic;
import org.fourstack.backoffice.enums.KycRequestType;
import org.fourstack.backoffice.exception.InvalidInputException;
import org.springframework.kafka.config.TopicBuilder;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Objects;

public final class BackOfficeUtil {
  private static ObjectMapper objectMapper;

  /**
   * Default Private Constructor
   */
  private BackOfficeUtil() {
  }

  public static String getCurrentTimeStamp() {
    OffsetDateTime dateTime = OffsetDateTime.now();
    DateTimeFormatter formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
    return dateTime.format(formatter);
  }

  public static boolean isCollectionNotNullOrEmpty(Collection<?> collection) {
    return collection != null && !collection.isEmpty();
  }

  public static boolean isNotNull(Object object) {
    return Objects.nonNull(object);
  }

  public static boolean isNull(Object object) {
    return Objects.isNull(object);
  }

  public static boolean isNotNullOrEmpty(String str) {
    return Objects.nonNull(str) && !str.isEmpty() && !str.isBlank();
  }

  private static ObjectMapper getInstance() {
    if (Objects.isNull(objectMapper)) {
      synchronized (BackOfficeUtil.class) {
        if (Objects.isNull(objectMapper)) {
          objectMapper = new ObjectMapper();
        }
      }
    }
    return objectMapper;
  }

  public static String convertToString(Object obj) {
    try {
      return getInstance().writeValueAsString(obj);
    } catch (JsonProcessingException e) {
      throw new InvalidInputException("", "Exception in converting the Object to json string");
    }
  }

  public static <T> T convertToObject(String str, Class<T> classObj) {
    try {
      return getInstance().readValue(str, classObj);
    } catch (JsonProcessingException e) {
      throw new InvalidInputException("", "Exception in converting the JSON string to Object");
    }
  }


  public static NewTopic createTopic(String name, int partitions, int replicaCount) {
    return TopicBuilder.name(name)
            .partitions(partitions)
            .replicas(replicaCount)
            .build();
  }

  public static String getCurrentDate() {
    LocalDate date = LocalDate.now();
    DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE;
    return date.format(formatter);
  }

  public static KycRequestType getKycRequestType(String requestType) {
    for (KycRequestType value : KycRequestType.values()) {
      if (value.getRequestType().equals(requestType)) {
        return value;
      }
    }
    return KycRequestType.UNKNOWN;
  }
}
