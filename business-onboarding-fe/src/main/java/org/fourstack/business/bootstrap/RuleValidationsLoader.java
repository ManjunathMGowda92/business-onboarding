package org.fourstack.business.bootstrap;

import org.fourstack.business.exceptions.DataLoadingException;
import org.fourstack.business.model.Validation;
import org.fourstack.business.model.Validations;
import org.fourstack.business.utils.BusinessUtil;
import org.fourstack.business.validator.ValidationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

@Service
public class RuleValidationsLoader implements CommandLineRunner {
    private static final Logger logger = LoggerFactory.getLogger(RuleValidationsLoader.class);

    @Override
    public void run(String... args) {
        loadXmlRuleValidations();
    }

    private void loadXmlRuleValidations() {
        logger.info("{} - Loading XML validations", this.getClass().getSimpleName());
        String validationsData = retrieveValidationsData();
        Validations validations = BusinessUtil.deserializeXML(validationsData, Validations.class);
        List<Validation> validationList = validations.getValidation();
        if (BusinessUtil.isCollectionNotNullOrEmpty(validationList)) {
            validationList.forEach(validation ->
                    ValidationManager.createValidationConfig(validation.getName(), validation));
        }
        logger.info("{} - XML validations loaded.", this.getClass().getSimpleName());
    }

    private String retrieveValidationsData() {
        try (InputStreamReader streamReader = getInputStreamReader("validations", "validations.xml");
             BufferedReader reader = new BufferedReader(streamReader)) {
            String line = reader.readLine();
            StringBuilder builder = new StringBuilder();
            while (line != null) {
                builder.append(line);
                line = reader.readLine();
            }
            return builder.toString();
        } catch (Exception exception) {
            throw new DataLoadingException("Exception in loading validations.xml : " + exception.getMessage(), exception);
        }
    }

    private InputStreamReader getInputStreamReader(String folder, String resourceName) {
        Class<? extends RuleValidationsLoader> classObj = this.getClass();
        logger.info("{} - Loading InputStreamReader for the file: {} from folder: {}",
                classObj.getSimpleName(), resourceName, folder);
        String filePath = folder.concat(File.separator).concat(resourceName);
        InputStream inputStream = classObj.getClassLoader().getResourceAsStream(filePath);
        assert inputStream != null;
        return new InputStreamReader(inputStream);
    }
}
