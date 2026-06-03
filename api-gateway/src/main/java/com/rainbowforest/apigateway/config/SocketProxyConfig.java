package com.rainbowforest.apigateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Cấu hình route Socket.IO qua Java DSL thay vì application.properties
 *
 * Lý do dùng Java DSL:
 * - Cần thêm filter RemoveRequestHeader để tránh Spring Session
 *   intercept request /socket.io/** và trả về 500
 * - Cần PreserveHostHeader để socket server nhận đúng host
 * - properties config không support đủ filter cho socket.io
 */
@Configuration
public class SocketProxyConfig {

    @Bean
    public RouteLocator socketRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
            .route("socket-io-route", r -> r
                .path("/socket.io/**")
                .filters(f -> f
                    // FIX: Xóa Cookie header để Spring Session không intercept
                    // và cố tìm session trong Redis → gây 500
                    .removeRequestHeader("Cookie")
                    // Giữ nguyên Host header để socket server nhận đúng
                    .preserveHostHeader()
                )
                .uri("http://localhost:5000")
            )
            .build();
    }
}
