package com.tt._2025.b077.huellaspormexico.config;

import io.github.wimdeblauwe.errorhandlingspringbootstarter.ApiErrorResponseCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Instant;

@Configuration
public class ApiErrorResponseConfig {

    @Bean
    public ApiErrorResponseCustomizer timestampErrorResponseCustomizer() {
        return response -> response.addErrorProperty("timestamp", Instant.now());
    }
}
