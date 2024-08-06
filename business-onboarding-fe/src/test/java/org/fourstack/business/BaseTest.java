package org.fourstack.business;

import org.fourstack.business.enums.ErrorCodeScenario;
import org.fourstack.business.enums.OperationStatus;
import org.fourstack.business.exceptions.MissingFieldException;
import org.fourstack.business.exceptions.ValidationException;
import org.fourstack.business.model.BusinessRegisterRequest;
import org.fourstack.business.model.ValidationResult;
import org.fourstack.business.utils.BusinessUtil;
import org.fourstack.business.utils.FileContentLoader;
import org.fourstack.business.utils.FileNameConstants;
import org.fourstack.business.validator.FieldFormatValidator;
import org.junit.jupiter.api.Assertions;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class BaseTest {
    private final FileContentLoader fileContentLoader = new FileContentLoader();
    @InjectMocks
    protected FieldFormatValidator formatValidator;

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
}
