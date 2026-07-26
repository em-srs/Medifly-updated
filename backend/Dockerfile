# Build stage
FROM eclipse-temurin:21-jdk-alpine AS build
WORKDIR /app
COPY . .
RUN ./gradlew bootJar --no-daemon

# Runtime stage
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/build/libs/medifly-backend-1.0.0.jar app.jar

ENV PORT=5000
EXPOSE 5000

ENTRYPOINT ["java", "-jar", "app.jar"]
