package org.fourstack.business.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
@ConfigurationProperties(prefix = "application")
@Data
public class ApplicationConfig {
    private Map<String, String> config;
}
