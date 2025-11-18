#!/bin/sh
set -e

# Get PORT from environment variable (Railway sets this dynamically)
# Default to 8080 if not set
PORT=${PORT:-8080}

# Export PORT so Spring Boot can use it
export PORT

# Set server.port system property explicitly for Spring Boot
# This ensures the application binds to Railway's assigned port
JAVA_OPTS="-Dserver.port=${PORT}"

# Log the port being used (helpful for debugging)
echo "Starting application on port: ${PORT}"

# Run the Spring Boot application with dynamic port configuration
exec java ${JAVA_OPTS} -jar app.jar

