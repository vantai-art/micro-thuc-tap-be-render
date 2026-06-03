# ================================
# Product Catalog Service — port 8810
# ================================
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline -q
COPY src ./src
RUN mvn clean package -DskipTests -q

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
COPY wait-for-eureka.sh wait-for-eureka.sh
RUN chmod +x wait-for-eureka.sh
# Thư mục lưu ảnh upload
RUN mkdir -p /app/uploads
EXPOSE 8810
ENV DEFAULT_PORT=8810
ENTRYPOINT ["sh", "wait-for-eureka.sh"]
