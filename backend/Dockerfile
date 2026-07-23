# Multi-stage Dockerfile for MediFly Java 21 Spring Boot Backend
FROM eclipse-temurin:21-jdk-alpine AS builder
WORKDIR /app

# Copy gradle wrapper and project files
COPY gradle gradle
COPY gradlew build.gradle settings.gradle ./
COPY src src

# Grant execution rights & build application jar
RUN chmod +x ./gradlew
RUN ./gradlew bootJar --no-daemon -x test

# Production Runtime Stage
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Copy artifact from builder stage
COPY --from=builder /app/build/libs/*.jar app.jar

# Expose Spring Boot port
EXPOSE 5000

# Run Spring Boot Application
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "app.jar"]
