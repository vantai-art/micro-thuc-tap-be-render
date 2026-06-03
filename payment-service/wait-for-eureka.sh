#!/bin/sh
# Chờ Eureka healthy trước khi start service
# EUREKA_URL phải được set qua env var

EUREKA_HEALTH_URL="${EUREKA_CLIENT_SERVICEURL_DEFAULTZONE%/eureka/}"
EUREKA_HEALTH_URL="${EUREKA_HEALTH_URL%/eureka}"
EUREKA_HEALTH_URL="${EUREKA_HEALTH_URL}/actuator/health"

echo "⏳ Chờ Eureka tại: $EUREKA_HEALTH_URL"

MAX_WAIT=180  # chờ tối đa 3 phút
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

exec java -Dserver.port=${PORT:-$DEFAULT_PORT} -jar app.jar
