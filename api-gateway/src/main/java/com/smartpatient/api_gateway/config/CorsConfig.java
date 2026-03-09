package com.smartpatient.api_gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.List;

/**
 * Clean CORS Strategy for Spring Cloud Gateway WebMVC.
 * 
 * Instead of hacking headers and response wrappers, we use Spring's native
 * CorsFilter.
 * Crucially, we ONLY register CORS for routes that DO NOT have their own CORS
 * configuration
 * (like VitalReports-AI, IoT, and Chatbot).
 * 
 * We INTENTIONALLY DO NOT register CORS for MainService routes (/api/doctor/**,
 * etc.)
 * because MainService handles its own CORS natively. This completely eliminates
 * the
 * duplicate header issue while ensuring all endpoints work.
 */
@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:5173"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);
        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        // Apply Gateway CORS to ALL routes
        // MainService CORS must be DISABLED to prevent duplicate headers
        source.registerCorsConfiguration("/**", config);

        return new CorsFilter(source);
    }
}
