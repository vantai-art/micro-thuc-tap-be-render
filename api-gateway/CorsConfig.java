package com.rainbowforest.apigateway.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * CORS Configuration cho API Gateway.
 *
 * Thêm origin mới:
 *   - Dev: thêm vào STATIC_ORIGINS bên dưới
 *   - Production: set env var FRONTEND_URL trên Render dashboard
 *     (nhiều URL cách nhau bằng dấu phẩy, vd: https://app1.onrender.com,https://app2.com)
 */
@Configuration
public class CorsConfig {

    // Origins cố định (local dev + các Render service đã biết)
    private static final List<String> STATIC_ORIGINS = Arrays.asList(
            "http://localhost:3000",
            "http://localhost:8081",
            "http://localhost:5173",
            "http://localhost:19006",
            "http://localhost:4000",
            "http://10.0.2.2:8081",
            "http://127.0.0.1:3000",
            "http://127.0.0.1:8081"
    );

    // Đọc từ env var FRONTEND_URL — có thể nhiều URL cách nhau bằng dấu phẩy
    @Value("${app.frontend.url:}")
    private String frontendUrlEnv;

    @Bean
    public CorsWebFilter corsWebFilter() {
        List<String> allowedOrigins = new ArrayList<>(STATIC_ORIGINS);

        // Thêm các URL từ env var (hỗ trợ nhiều URL)
        if (frontendUrlEnv != null && !frontendUrlEnv.isBlank()) {
            for (String url : frontendUrlEnv.split(",")) {
                String trimmed = url.trim();
                if (!trimmed.isEmpty() && !allowedOrigins.contains(trimmed)) {
                    allowedOrigins.add(trimmed);
                }
            }
        }

        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(allowedOrigins);
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        config.setAllowedHeaders(Arrays.asList("*"));
        config.setAllowCredentials(true);
        config.setMaxAge(3600L);
        config.setExposedHeaders(Arrays.asList("Authorization", "Location", "Access-Control-Allow-Origin"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return new CorsWebFilter(source);
    }
}
