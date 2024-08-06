package org.fourstack.business.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.http.HttpClient;
import java.time.Duration;
import java.time.temporal.ChronoUnit;

@Configuration
public class BusinessServiceConfig {

    @Bean(name = "httpClient")
    public HttpClient httpClient(){
        return HttpClient.newBuilder()
                .connectTimeout(Duration.of(60, ChronoUnit.SECONDS))
                .build();
    }
}
