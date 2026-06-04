# Hướng dẫn Deploy lên Fly.io

## Chuẩn bị trước (làm 1 lần duy nhất)

### 1. Cài Fly CLI và đăng nhập
```bash
# macOS
brew install flyctl

# Windows (PowerShell)
iwr https://fly.io/install.ps1 -useb | iex

# Linux
curl -L https://fly.io/install.sh | sh

flyctl auth login
```

### 2. Lấy FLY_API_TOKEN để dùng cho GitHub Actions
```bash
flyctl auth token
```
→ Copy token này, vào GitHub repo → Settings → Secrets → Actions → New secret
→ Tên: `FLY_API_TOKEN`, Value: paste token vào

---

## Setup Database & Redis (free, không giới hạn thời gian)

### MySQL — Dùng Aiven (free tier)
1. Vào https://aiven.io → Đăng ký free
2. Tạo MySQL service → chọn free plan
3. Copy **Service URI** (dạng: `mysql://user:pass@host:port/defaultdb`)
4. Tạo 5 database riêng:
   - `users`
   - `product_catalog`
   - `recommendation_db`
   - `orders_db`
   - `payment_db`
   - `settings_db`

### Redis — Dùng Upstash (free tier)
1. Vào https://upstash.com → Đăng ký free
2. Tạo Redis database → chọn region Singapore
3. Copy **Endpoint**, **Port**, **Password**

---

## Tạo Fly.io apps (làm 1 lần duy nhất)

```bash
# Chạy từng lệnh này (không cần deploy ngay)
flyctl apps create ecom-eureka
flyctl apps create ecom-user-service
flyctl apps create ecom-product-catalog
flyctl apps create ecom-recommendation
flyctl apps create ecom-order-service
flyctl apps create ecom-payment-service
flyctl apps create ecom-setting-service
flyctl apps create ecom-socket-server
flyctl apps create ecom-api-gateway

# Tạo volume cho product-catalog (lưu ảnh upload)
flyctl volumes create product_uploads --app ecom-product-catalog --region sin --size 1
```

---

## Set Secrets cho từng service

### Eureka Server (không cần secret)
```bash
# Không cần set gì thêm
```

### User Service
```bash
flyctl secrets set \
  SPRING_DATASOURCE_URL="jdbc:mysql://HOST:PORT/users?createDatabaseIfNotExist=true&useSSL=true&serverTimezone=UTC" \
  SPRING_DATASOURCE_USERNAME="avnadmin" \
  SPRING_DATASOURCE_PASSWORD="YOUR_AIVEN_PASSWORD" \
  EUREKA_CLIENT_SERVICEURL_DEFAULTZONE="https://ecom-eureka.fly.dev/eureka/" \
  SPRING_MAIL_USERNAME="vantai909zk@gmail.com" \
  SPRING_MAIL_PASSWORD="pvhwdbhevrufzhle" \
  --app ecom-user-service
```

### Product Catalog Service
```bash
flyctl secrets set \
  SPRING_DATASOURCE_URL="jdbc:mysql://HOST:PORT/product_catalog?createDatabaseIfNotExist=true&useSSL=true&serverTimezone=UTC" \
  SPRING_DATASOURCE_USERNAME="avnadmin" \
  SPRING_DATASOURCE_PASSWORD="YOUR_AIVEN_PASSWORD" \
  EUREKA_CLIENT_SERVICE_URL_DEFAULTZONE="https://ecom-eureka.fly.dev/eureka/" \
  --app ecom-product-catalog
```

### Product Recommendation Service
```bash
flyctl secrets set \
  SPRING_DATASOURCE_URL="jdbc:mysql://HOST:PORT/recommendation_db?createDatabaseIfNotExist=true&useSSL=true&serverTimezone=UTC" \
  SPRING_DATASOURCE_USERNAME="avnadmin" \
  SPRING_DATASOURCE_PASSWORD="YOUR_AIVEN_PASSWORD" \
  EUREKA_CLIENT_SERVICE_URL_DEFAULTZONE="https://ecom-eureka.fly.dev/eureka/" \
  --app ecom-recommendation
```

### Order Service
```bash
flyctl secrets set \
  SPRING_DATASOURCE_URL="jdbc:mysql://HOST:PORT/orders_db?createDatabaseIfNotExist=true&useSSL=true&serverTimezone=UTC" \
  SPRING_DATASOURCE_USERNAME="avnadmin" \
  SPRING_DATASOURCE_PASSWORD="YOUR_AIVEN_PASSWORD" \
  EUREKA_CLIENT_SERVICE_URL_DEFAULTZONE="https://ecom-eureka.fly.dev/eureka/" \
  SPRING_DATA_REDIS_HOST="YOUR_UPSTASH_ENDPOINT" \
  SPRING_DATA_REDIS_PASSWORD="YOUR_UPSTASH_PASSWORD" \
  --app ecom-order-service
```

### Payment Service
```bash
flyctl secrets set \
  SPRING_DATASOURCE_URL="jdbc:mysql://HOST:PORT/payment_db?createDatabaseIfNotExist=true&useSSL=true&allowPublicKeyRetrieval=true" \
  SPRING_DATASOURCE_USERNAME="avnadmin" \
  SPRING_DATASOURCE_PASSWORD="YOUR_AIVEN_PASSWORD" \
  EUREKA_CLIENT_SERVICE_URL_DEFAULTZONE="https://ecom-eureka.fly.dev/eureka/" \
  VNPAY_TMN_CODE="LQ1203S3" \
  VNPAY_HASH_SECRET="30F753TNRCBSBBHZ8XUWT05V3UP84VSN" \
  --app ecom-payment-service
```

### Setting Service
```bash
flyctl secrets set \
  SPRING_DATASOURCE_URL="jdbc:mysql://HOST:PORT/settings_db?createDatabaseIfNotExist=true&useSSL=true&serverTimezone=UTC" \
  SPRING_DATASOURCE_USERNAME="avnadmin" \
  SPRING_DATASOURCE_PASSWORD="YOUR_AIVEN_PASSWORD" \
  EUREKA_CLIENT_SERVICEURL_DEFAULTZONE="https://ecom-eureka.fly.dev/eureka/" \
  --app ecom-setting-service
```

### API Gateway
```bash
flyctl secrets set \
  EUREKA_CLIENT_SERVICEURL_DEFAULTZONE="https://ecom-eureka.fly.dev/eureka/" \
  --app ecom-api-gateway
```

---

## Copy fly.toml vào đúng vị trí trong repo

```
micro-thuc-tap-be/
├── eureka-server/
│   └── fly.toml          ← copy từ fly-configs/eureka-server/
├── user-service/
│   └── fly.toml          ← copy từ fly-configs/user-service/
├── product-catalog-service/
│   └── fly.toml          ← copy từ fly-configs/product-catalog-service/
├── product-recommendation-service/
│   └── fly.toml          ← copy từ fly-configs/product-recommendation-service/
├── order-service/
│   └── fly.toml          ← copy từ fly-configs/order-service/
├── payment-service/
│   └── fly.toml          ← copy từ fly-configs/payment-service/
├── setting-service/
│   └── fly.toml          ← copy từ fly-configs/setting-service/
├── socket-server/
│   └── fly.toml          ← copy từ fly-configs/socket-server/
├── api-gateway/
│   └── fly.toml          ← copy từ fly-configs/api-gateway/
└── .github/
    └── workflows/
        ├── deploy-eureka.yml
        ├── deploy-user.yml
        ├── deploy-product-catalog.yml
        ├── deploy-product-recommendation.yml
        ├── deploy-order.yml
        ├── deploy-payment.yml
        ├── deploy-setting.yml
        ├── deploy-socket.yml
        └── deploy-api-gateway.yml
```

---

## Deploy lần đầu (thủ công, đúng thứ tự)

```bash
# Bước 1: Eureka trước
cd eureka-server
flyctl deploy --remote-only
cd ..

# Chờ Eureka healthy (~2 phút), kiểm tra:
# https://ecom-eureka.fly.dev/actuator/health

# Bước 2: Tất cả service song song
cd user-service && flyctl deploy --remote-only && cd ..
cd product-catalog-service && flyctl deploy --remote-only && cd ..
cd product-recommendation-service && flyctl deploy --remote-only && cd ..
cd order-service && flyctl deploy --remote-only && cd ..
cd payment-service && flyctl deploy --remote-only && cd ..
cd setting-service && flyctl deploy --remote-only && cd ..
cd socket-server && flyctl deploy --remote-only && cd ..

# Bước 3: API Gateway cuối cùng
cd api-gateway && flyctl deploy --remote-only && cd ..
```

---

## Sau khi deploy xong

| Service | URL |
|---------|-----|
| Eureka Dashboard | https://ecom-eureka.fly.dev |
| API Gateway | https://ecom-api-gateway.fly.dev |
| Socket Server | wss://ecom-socket-server.fly.dev |

### Cập nhật CORS trong api-gateway (CorsConfig.java)
Thêm URL frontend production vào `ALLOWED_ORIGINS`:
```java
"https://YOUR_FRONTEND_URL",
```

### Từ lần sau
Chỉ cần `git push` → GitHub Actions tự động deploy service tương ứng.

---

## Lưu ý quan trọng

- **Free tier Fly.io**: 3 shared-cpu VMs miễn phí, service thứ 4 trở đi tính tiền ~$1.94/tháng/VM
- Project có 9 services → cần upgrade plan hoặc tạm thời chỉ chạy các service cần thiết
- Aiven MySQL free: 1 node, 5GB storage, đủ để demo
- Upstash Redis free: 10.000 requests/ngày, đủ để demo
