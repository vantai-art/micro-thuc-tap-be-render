#!/bin/sh
# Khởi động Spring Boot ngay để Render detect port
# Spring Boot sẽ tự retry kết nối Eureka

echo "🚀 Khởi động service với JVM tối ưu..."
exec java \
  -Xms128m \
  -Xmx384m \
  -XX:+UseSerialGC \
  -XX:MaxMetaspaceSize=128m \
  -Dspring.jpa.properties.hibernate.temp.use_jdbc_metadata_defaults=false \
  -Dserver.port=${PORT:-$DEFAULT_PORT} \
  -jar app.jar
