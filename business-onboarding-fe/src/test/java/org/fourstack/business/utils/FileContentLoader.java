package org.fourstack.business.utils;

import org.fourstack.business.exceptions.DataLoadingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;

public class FileContentLoader {
    private static final Logger logger = LoggerFactory.getLogger(FileContentLoader.class);

    public String loadContent(String filePath) {
        try (InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(filePath)) {
            if (inputStream == null) {
                throw new DataLoadingException("Unable to load the file content : " + filePath);
            }
            logger.info("Loading contents of file - {}", filePath);
            return new String(inputStream.readAllBytes());
        } catch (Exception exception) {
            throw new DataLoadingException("Unable to load the file content : " + filePath);
        }
    }
}
