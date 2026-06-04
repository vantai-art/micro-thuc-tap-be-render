# ============================================================
# HƯỚNG DẪN CÀI ĐẶT ENVIRONMENT VARIABLES TRÊN RENDER
# ============================================================
# Vào Render Dashboard → chọn service → Environment → Add Env Var
# ============================================================

# ─────────────────────────────────────────────────────
# 1. EUREKA SERVER  (ecom-eureka)
# ─────────────────────────────────────────────────────
# Không cần thêm env var đặc biệt — service tự chạy độc lập.
# Render tự inject PORT.


# ─────────────────────────────────────────────────────
# 2. USER SERVICE  (ecom-user-service)
# ─────────────────────────────────────────────────────
EUREKA_SERVER_URL=https://ecom-eureka.onrender.com/eureka/
SPRING_DATASOURCE_URL=jdbc:postgresql://<aiven_host>:<port>/defaultdb?sslmode=require
SPRING_DATASOURCE_USERNAME=avnadmin
SPRING_DATASOURCE_PASSWORD=<your_aiven_password>
MAIL_USERNAME=your_gmail@gmail.com
MAIL_PASSWORD=xxxx xxxx xxxx xxxx        # Gmail App Password (không phải mật khẩu Gmail)
FRONTEND_URL=https://YOUR_FRONTEND.onrender.com


# ─────────────────────────────────────────────────────
# 3. PRODUCT CATALOG SERVICE  (ecom-product-catalog)
# ─────────────────────────────────────────────────────
EUREKA_SERVER_URL=https://ecom-eureka.onrender.com/eureka/
SPRING_DATASOURCE_URL=jdbc:postgresql://<aiven_host>:<port>/defaultdb?sslmode=require
SPRING_DATASOURCE_USERNAME=avnadmin
SPRING_DATASOURCE_PASSWORD=<your_aiven_password>
APP_BASE_URL=https://ecom-product-catalog.onrender.com
# Nếu dùng Cloudinary cho ảnh (khuyến nghị):
# CLOUDINARY_URL=cloudinary://api_key:api_secret@cloud_name


# ─────────────────────────────────────────────────────
# 4. PRODUCT RECOMMENDATION SERVICE  (ecom-recommendation)
# ─────────────────────────────────────────────────────
EUREKA_SERVER_URL=https://ecom-eureka.onrender.com/eureka/
SPRING_DATASOURCE_URL=jdbc:postgresql://<aiven_host>:<port>/defaultdb?sslmode=require
SPRING_DATASOURCE_USERNAME=avnadmin
SPRING_DATASOURCE_PASSWORD=<your_aiven_password>


# ─────────────────────────────────────────────────────
# 5. ORDER SERVICE  (ecom-order)
# ─────────────────────────────────────────────────────
EUREKA_SERVER_URL=https://ecom-eureka.onrender.com/eureka/
SPRING_DATASOURCE_URL=jdbc:postgresql://<aiven_host>:<port>/defaultdb?sslmode=require
SPRING_DATASOURCE_USERNAME=avnadmin
SPRING_DATASOURCE_PASSWORD=<your_aiven_password>

# Redis — dùng Upstash (free tier, hỗ trợ SSL)
# Đăng ký tại: https://upstash.com → tạo Redis database → copy thông tin bên dưới
REDIS_HOST=<your>.upstash.io
REDIS_PORT=6379
REDIS_PASSWORD=<upstash_password>
REDIS_SSL=true


# ─────────────────────────────────────────────────────
# 6. PAYMENT SERVICE  (ecom-payment-service)
# ─────────────────────────────────────────────────────
EUREKA_SERVER_URL=https://ecom-eureka.onrender.com/eureka/
SPRING_DATASOURCE_URL=jdbc:postgresql://<aiven_host>:<port>/defaultdb?sslmode=require
SPRING_DATASOURCE_USERNAME=avnadmin
SPRING_DATASOURCE_PASSWORD=<your_aiven_password>
VNPAY_TMN_CODE=<your_vnpay_tmn_code>
VNPAY_HASH_SECRET=<your_vnpay_hash_secret>
API_GATEWAY_URL=https://ecom-api-gateway-phoe.onrender.com
FRONTEND_URL=https://YOUR_FRONTEND.onrender.com


# ─────────────────────────────────────────────────────
# 7. SETTING SERVICE  (ecom-setting-service)
# ─────────────────────────────────────────────────────
EUREKA_SERVER_URL=https://ecom-eureka.onrender.com/eureka/
SPRING_DATASOURCE_URL=jdbc:postgresql://<aiven_host>:<port>/defaultdb?sslmode=require
SPRING_DATASOURCE_USERNAME=avnadmin
SPRING_DATASOURCE_PASSWORD=<your_aiven_password>


# ─────────────────────────────────────────────────────
# 8. API GATEWAY  (ecom-api-gateway-phoe)
# ─────────────────────────────────────────────────────
EUREKA_SERVER_URL=https://ecom-eureka.onrender.com/eureka/
FRONTEND_URL=https://YOUR_FRONTEND.onrender.com
SOCKET_SERVER_URL=https://ecom-socket-server.onrender.com


# ─────────────────────────────────────────────────────
# 9. SOCKET SERVER  (ecom-socket-server)
# ─────────────────────────────────────────────────────
# Không cần env var thêm — PORT được Render inject tự động.


# ============================================================
# LƯU Ý QUAN TRỌNG
# ============================================================
#
# 1. EUREKA_SERVER_URL:
#    Dùng URL public của Render (https://...) vì mỗi service là
#    container riêng, không dùng được localhost hay hostname Docker.
#
# 2. Database:
#    Nếu dùng Aiven (PostgreSQL), bật sslmode=require trong JDBC URL.
#    Nếu dùng Render PostgreSQL, URL có dạng:
#    jdbc:postgresql://dpg-xxx.oregon-postgres.render.com:5432/dbname
#
# 3. Redis:
#    Upstash free tier: 10K req/ngày, SSL bắt buộc.
#    Khi dùng Upstash, REDIS_SSL=true là bắt buộc.
#
# 4. Render free tier sleep:
#    Service ngủ sau 15 phút không có request → cold start ~30-60s.
#    Eureka sẽ xóa service ngủ sau ~90s không heartbeat.
#    → Các service sẽ tự re-register khi thức dậy.
#
# 5. VNPAY return URL:
#    Phải là URL public (https), không thể dùng localhost.
#    Đặt API_GATEWAY_URL = URL thật của api-gateway trên Render.
# ============================================================
