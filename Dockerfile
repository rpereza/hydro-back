# Multi-stage build for Spring Boot application
FROM eclipse-temurin:21-jdk-alpine AS build

WORKDIR /app

# Copy Maven wrapper and pom.xml
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# Make mvnw executable
RUN chmod +x ./mvnw

# Download dependencies (this layer will be cached if pom.xml doesn't change)
RUN ./mvnw dependency:go-offline -B

# Copy source code
COPY src ./src

# Build the application
RUN ./mvnw clean package -DskipTests -B

# Runtime stage
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Install curl for health checks
RUN apk add --no-cache curl

# Create a non-root user
RUN addgroup -S spring && adduser -S spring -G spring

# Copy scripts and make them executable (before switching to non-root user)
COPY docker-entrypoint.sh /app/docker-entrypoint.sh
COPY docker-healthcheck.sh /app/docker-healthcheck.sh
RUN chmod +x /app/docker-entrypoint.sh /app/docker-healthcheck.sh && \
    chown -R spring:spring /app

# Switch to non-root user
USER spring:spring

# Copy the JAR from build stage
COPY --from=build --chown=spring:spring /app/target/*.jar app.jar

# Expose port (Railway will set PORT env variable dynamically)
# EXPOSE is just documentation - Railway will map the actual port
EXPOSE 8080

# Health check using dynamic PORT via script
# Railway will handle health checks through their platform, but this provides container-level health
HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=3 \
  CMD /app/docker-healthcheck.sh

# Run the application with entrypoint script that handles dynamic PORT
ENTRYPOINT ["/app/docker-entrypoint.sh"]

