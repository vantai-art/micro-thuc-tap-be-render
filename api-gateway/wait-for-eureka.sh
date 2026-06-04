#!/bin/sh
EUREKA_BASE="${EUREKA_CLIENT_SERVICEURL_DEFAULTZONE:-$EUREKA_CLIENT_SERVICE_URL_DEFAULTZONE}"
EUREKA_BASE="${EUREKA_BASE%/eureka/}"
EUREKA_BASE="${EUREKA_BASE%/eureka}"
EUREKA_HEALTH_URL="${EUREKA_BASE}/actuator/health"

echo "⏳ [API Gateway] Chờ Eureka tại: $EUREKA_HEALTH_URL"

MAX_WAIT=300
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

echo "⏳ Chờ thêm 90s để các service đăng ký vào Eureka..."
sleep 90

echo "🚀 Khởi động API Gateway..."
exec java \
  -Xms128m \
  -Xmx384m \
  -XX:+UseSerialGC \
  -XX:MaxMetaspaceSize=128m \
  -Dserver.port=${PORT:-8080} \
  -jar app.jar
