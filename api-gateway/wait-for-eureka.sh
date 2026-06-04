#!/bin/sh
echo "🚀 Khởi động API Gateway..."
exec java \
  -Xms128m \
  -Xmx384m \
  -XX:+UseSerialGC \
  -XX:MaxMetaspaceSize=128m \
  -Dserver.port=${PORT:-8080} \
  -jar app.jar
