#!/bin/sh
# API Gateway: chờ Eureka healthy, rồi chờ thêm để các service kịp đăng ký

EUREKA_HEALTH_URL="${EUREKA_CLIENT_SERVICEURL_DEFAULTZONE%/eureka/}"
EUREKA_HEALTH_URL="${EUREKA_HEALTH_URL%/eureka}"
EUREKA_HEALTH_URL="${EUREKA_HEALTH_URL}/actuator/health"

echo "⏳ [API Gateway] Chờ Eureka tại: $EUREKA_HEALTH_URL"

MAX_WAIT=180
WAITED=0
INTERVAL=10

while [ $WAITED -lt $MAX_WAIT ]; do
  STATUS=$(wget -qO- "$EUREKA_HEALTH_URL" 2>/dev/null | grep -o '"status":"UP"')
  if [ "$STATUS" = '"status":"UP"' ]; then
    echo "✅ Eureka sẵn sàng!"
    break
  fi
  echo "⌛ Chờ Eureka... (${WAITED}s/${MAX_WAIT}s)"
  sleep $INTERVAL
  WAITED=$((WAITED + INTERVAL))
done

# Chờ thêm 60s để các microservice kịp đăng ký vào Eureka
echo "⏳ Chờ thêm 60s để các service đăng ký vào Eureka..."
sleep 60

echo "🚀 Khởi động API Gateway..."
exec java -Dserver.port=${PORT:-8080} -jar app.jar
