#!/bin/sh
# ============================================================
# wait-for-eureka.sh — dùng cho tất cả Spring Boot services
# 
# Khi Render stop container:
#   1. Render gửi SIGTERM
#   2. Script bắt SIGTERM → gửi graceful shutdown về JVM
#   3. Spring Boot EurekaClient gửi deregister về Eureka Server
#   4. Container thoát sạch — KHÔNG để lại stale instance
# ============================================================

echo "🚀 Khởi động service (port: ${PORT:-$DEFAULT_PORT})..."

# Khởi động JVM trong background
exec java \
  -Xms128m \
  -Xmx384m \
  -XX:+UseSerialGC \
  -XX:MaxMetaspaceSize=128m \
  -Dspring.jpa.properties.hibernate.temp.use_jdbc_metadata_defaults=false \
  -Dserver.port=${PORT:-$DEFAULT_PORT} \
  -jar app.jar &

JVM_PID=$!

# Bắt SIGTERM từ Render → forward cho JVM (graceful shutdown)
trap "echo '⏹ SIGTERM nhận được — đang graceful shutdown...'; kill -TERM $JVM_PID; wait $JVM_PID" TERM INT

# Chờ JVM kết thúc
wait $JVM_PID
EXIT_CODE=$?
echo "✅ JVM đã thoát với code: $EXIT_CODE"
exit $EXIT_CODE
