package com.quma.app.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // Apply to all paths
                .allowedOrigins("*") // Specify allowed origins
                .allowedMethods("*") // Specify allowed methods
                .allowedHeaders("*") // Specify allowed headers
                .allowCredentials(true) // Allow sending cookies, authorization headers, etc.
                .maxAge(3600); // Cache the CORS preflight response for 1 hour
    }
}
