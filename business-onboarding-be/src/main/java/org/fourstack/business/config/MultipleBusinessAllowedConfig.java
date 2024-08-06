package org.fourstack.business.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
@ConfigurationProperties(prefix = "lei")
@Data
public class MultipleBusinessAllowedConfig {
    private Map<String, String> multipleBusinessConfig;
}
