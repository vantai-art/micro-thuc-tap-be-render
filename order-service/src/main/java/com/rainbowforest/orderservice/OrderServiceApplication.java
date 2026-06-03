// package com.rainbowforest.orderservice;

// import org.springframework.boot.SpringApplication;
// import org.springframework.boot.autoconfigure.SpringBootApplication;
// import org.springframework.cloud.openfeign.EnableFeignClients;
// import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

// @SpringBootApplication
// @EnableFeignClients
// @EnableRedisHttpSession
// public class OrderServiceApplication {
//     public static void main(String[] args) {
//         SpringApplication.run(OrderServiceApplication.class, args);
//     }
// }

package com.rainbowforest.orderservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

// FIX: Đã xóa @EnableRedisHttpSession
// Annotation này bắt buộc Redis phải chạy cho MỌI request HTTP
// → nếu Redis down hoặc chưa start → 500 trên tất cả endpoint (kể cả /order/direct, /order/table)
// Order-service không cần session HTTP (auth do api-gateway + user-service xử lý)
// Redis chỉ dùng cho Cart (CartRedisRepository) — vẫn hoạt động bình thường qua RedisTemplate

@SpringBootApplication
@EnableFeignClients
public class OrderServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(OrderServiceApplication.class, args);
    }
}