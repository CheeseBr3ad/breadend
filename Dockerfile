# ==========================================================
# 1️⃣ Build stage — use Maven with Java 21
# ==========================================================
FROM maven:3.9.9-eclipse-temurin-21 AS build

WORKDIR /app

# Copy pom.xml first to leverage Docker caching
COPY pom.xml ./
RUN mvn dependency:go-offline -B

# Copy source and build the jar
COPY src ./src
RUN mvn clean package -DskipTests

# ==========================================================
# 2️⃣ Runtime stage — lightweight JRE with security optimizations
# ==========================================================
FROM eclipse-temurin:21-jre-alpine

# Install security updates and required packages
RUN apk update && apk upgrade && \
    apk add --no-cache curl && \
    rm -rf /var/cache/apk/*

# Create non-root user for security
RUN addgroup -g 1001 -S spring && \
    adduser -u 1001 -S spring -G spring

WORKDIR /app

# Copy built JAR from the build stage
COPY --from=build /app/target/*.jar app.jar

# Change ownership to non-root user
RUN chown spring:spring app.jar

# Switch to non-root user
USER spring

# Expose the Spring Boot port
EXPOSE 8080

# Environment variables with defaults
ENV DB_HOST=db.imdrhifkhpbmoxfmylhi.supabase.co \
    DB_PORT=5432 \
    DB_NAME=postgres \
    DB_USER=postgres \
    SPRING_PROFILES_ACTIVE=docker \
    JAVA_OPTS="-Xmx512m -Xms256m -XX:+UseG1GC -XX:+UseContainerSupport"

# DB_PASSWORD must be provided as environment variable at runtime

# JVM optimizations for containers
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
