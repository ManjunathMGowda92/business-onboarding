package org.fourstack.business;

import org.fourstack.business.enums.ErrorCodeScenario;
import org.fourstack.business.enums.OperationStatus;
import org.fourstack.business.exceptions.MissingFieldException;
import org.fourstack.business.exceptions.ValidationException;
import org.fourstack.business.model.BusinessRegisterRequest;
import org.fourstack.business.model.CommonData;
import org.fourstack.business.model.ValidationResult;
import org.fourstack.business.utils.BusinessUtil;
import org.fourstack.business.utils.FileContentLoader;
import org.fourstack.business.utils.FileNameConstants;
import org.fourstack.business.validator.FieldFormatValidator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Random;

@SpringBootTest
@EmbeddedKafka(ports = {9092})
public abstract class BaseTest {
    private static final String[] ALPHABETS = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z",
            "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"};
    private static final int[] INTEGERS = {1, 2, 3, 4, 5, 6, 7, 8, 9, 0};
    private static final Random random = new Random();

    private final FileContentLoader fileContentLoader = new FileContentLoader();
    @InjectMocks
    protected FieldFormatValidator formatValidator;

    @BeforeEach
    public void initializeTests() {
        ReflectionTestUtils.setField(formatValidator, "requestTimeOut", 180);
    }

    public BusinessRegisterRequest getBusinessRequest() {
        String fileContent = getFileContent(FileNameConstants.BUSINESS_REGISTER_REQUEST);
        return BusinessUtil.convertToObject(fileContent, BusinessRegisterRequest.class);
    }

    private String getFileContent(String filePath) {
        return fileContentLoader.loadContent(filePath);
    }

    protected void assertValidationException(ValidationException exception, ErrorCodeScenario scenarioCode,
                                             String fieldName) {
        Assertions.assertNotNull(exception);
        Assertions.assertNotNull(exception.getMessage());
        Assertions.assertNotNull(exception.getErrorCode());
        Assertions.assertNotNull(exception.getErrorMessage());
        Assertions.assertEquals(scenarioCode.getErrorCode(), exception.getErrorCode());
        Assertions.assertEquals(scenarioCode.getErrorMsg(), exception.getErrorMessage());
        if (null != exception.getFieldName()) {
            Assertions.assertEquals(fieldName, exception.getFieldName());
        }
    }

    protected void assertMissingFieldException(MissingFieldException exception, String fieldName) {
        Assertions.assertNotNull(exception);
        Assertions.assertNotNull(exception.getMessage());
        Assertions.assertEquals(fieldName, exception.getFieldName());
    }

    protected void assertSuccessValidation(ValidationResult result) {
        Assertions.assertNotNull(result);
        Assertions.assertEquals(OperationStatus.SUCCESS, result.status());
    }

    protected void setTimeStamp(CommonData commonData) {
        commonData.getHead().setTs(BusinessUtil.getCurrentTimeStamp());
        commonData.getTxn().setTs(BusinessUtil.getCurrentTimeStamp());
    }

    protected String alphaNumericString(int length, String prefix) {
        StringBuilder builder = new StringBuilder();
        if (BusinessUtil.isNotNullOrEmpty(prefix)) {
            builder.append(prefix);
        }
        int i = 0;
        while (builder.length() < length) {
            if (i % 2 == 0) {
                builder.append(ALPHABETS[random.nextInt(ALPHABETS.length)]);
            } else {
                builder.append(INTEGERS[random.nextInt(INTEGERS.length)]);
            }
            i++;
        }
        return builder.toString();
    }
}
