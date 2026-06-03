# ================================
# User Service — port 8811
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
EXPOSE 8811
ENV DEFAULT_PORT=8811
ENTRYPOINT ["sh", "wait-for-eureka.sh"]
