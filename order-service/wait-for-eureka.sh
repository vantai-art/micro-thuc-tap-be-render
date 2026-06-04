#!/bin/sh
# Chờ Eureka healthy trước khi start service

# Thử cả 2 key env var (tùy service dùng key nào)
EUREKA_BASE="${EUREKA_CLIENT_SERVICEURL_DEFAULTZONE:-$EUREKA_CLIENT_SERVICE_URL_DEFAULTZONE}"
EUREKA_BASE="${EUREKA_BASE%/eureka/}"
EUREKA_BASE="${EUREKA_BASE%/eureka}"
EUREKA_HEALTH_URL="${EUREKA_BASE}/actuator/health"

echo "⏳ Chờ Eureka tại: $EUREKA_HEALTH_URL"

MAX_WAIT=300  # chờ tối đa 5 phút
WAITED=0
INTERVAL=10

while [ $WAITED -lt $MAX_WAIT ]; do
  STATUS=$(wget -qO- "$EUREKA_HEALTH_URL" 2>/dev/null | grep -o '"status":"UP"')
  if [ "$STATUS" = '"status":"UP"' ]; then
    echo "✅ Eureka đã sẵn sàng! Khởi động service..."
    break
  fi
  echo "⌛ Eureka chưa ready, thử lại sau ${INTERVAL}s... (${WAITED}s/${MAX_WAIT}s)"
  sleep $INTERVAL
  WAITED=$((WAITED + INTERVAL))
done

if [ $WAITED -ge $MAX_WAIT ]; then
  echo "⚠️ Timeout chờ Eureka, khởi động anyway..."
fi

# JVM options tối ưu cho free tier 512MB RAM
exec java \
  -Xms128m \
  -Xmx384m \
  -XX:+UseSerialGC \
  -XX:MaxMetaspaceSize=128m \
  -Dspring.jpa.properties.hibernate.temp.use_jdbc_metadata_defaults=false \
  -Dserver.port=${PORT:-$DEFAULT_PORT} \
  -jar app.jar
