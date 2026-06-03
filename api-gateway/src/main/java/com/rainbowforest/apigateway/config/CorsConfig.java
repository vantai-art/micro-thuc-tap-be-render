package com.rainbowforest.apigateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

/**
 * ========================================
 * CORS Configuration cho API Gateway
 * ========================================
 *
 * Lý do cần file này:
 * - application.properties dùng allowed-origins=* (wildcard)
 * - Khi frontend gửi credentials: 'include' (cookie/session),
 * browser yêu cầu origin phải được chỉ định rõ ràng, KHÔNG được dùng *
 * - File này override config trong properties, cho phép cụ thể từng origin
 *
 * Thêm origin mới: bổ sung vào danh sách ALLOWED_ORIGINS bên dưới
 */
@Configuration
public class CorsConfig {

        // ✅ Danh sách origin được phép — thêm origin mới vào đây
        private static final List<String> ALLOWED_ORIGINS = Arrays.asList(
                        // --- Local dev ---
                        "http://localhost:8081",
                        "http://localhost:3000",
                        "http://frontend:3000",
                        "http://localhost:19006",
                        "http://10.0.2.2:8081",
                        "http://127.0.0.1:8081",
                        "http://127.0.0.1:3000",
                        "http://localhost:5173",
                        "http://localhost:4000",
                        "http://localhost:8080",
                        // --- Render.com production ---
                        // ⚠️ Thay YOUR_FRONTEND_APP bằng tên app frontend thật của bạn
                        "https://YOUR_FRONTEND_APP.onrender.com",
                        "https://ecom-api-gateway.onrender.com");

        @Bean
        public CorsWebFilter corsWebFilter() {
                CorsConfiguration config = new CorsConfiguration();
                config.setAllowedOrigins(ALLOWED_ORIGINS);
                config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
                config.setAllowedHeaders(Arrays.asList("*"));
                config.setAllowCredentials(true);
                config.setMaxAge(3600L);

                // Expose headers cần thiết cho frontend
                config.setExposedHeaders(Arrays.asList("Authorization", "Location", "Access-Control-Allow-Origin"));

                UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
                source.registerCorsConfiguration("/**", config);
                return new CorsWebFilter(source);
        }
}