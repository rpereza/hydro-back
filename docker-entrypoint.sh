#!/bin/sh
set -e

# Get PORT from environment variable (Railway sets this dynamically)
# Default to 8080 if not set
PORT=${PORT:-8080}

# Export PORT so Spring Boot can use it
export PORT

# Memory optimization for 1GB RAM limit
# -Xmx512m: Maximum heap size (512MB, leaving ~500MB for non-heap, OS, and buffer)
# -Xms256m: Initial heap size (256MB, reduces GC overhead)
# -XX:+UseG1GC: Use G1 garbage collector (efficient for low memory)
# -XX:MaxGCPauseMillis=200: Target max GC pause time
# -XX:+UseStringDeduplication: Deduplicate strings to save memory
# -XX:+OptimizeStringConcat: Optimize string concatenation
# -XX:+UseCompressedOops: Use compressed object pointers (saves memory on 64-bit)
# -XX:+UseCompressedClassPointers: Compress class pointers
# -XX:MaxMetaspaceSize=128m: Limit metaspace (class metadata)
# -XX:ReservedCodeCacheSize=64m: Limit code cache
# -XX:+ExitOnOutOfMemoryError: Exit if OOM (better than hanging)
# -XX:+HeapDumpOnOutOfMemoryError: Generate heap dump on OOM for debugging
# -XX:HeapDumpPath=/tmp: Where to save heap dumps
# -Djava.security.egd=file:/dev/./urandom: Faster startup (non-blocking random)
# -Dspring.jmx.enabled=false: Disable JMX (saves memory)
# -Dspring.backgroundpreinitializer.ignore=true: Disable background pre-initialization
JAVA_OPTS="-Xmx512m -Xms256m -XX:+UseG1GC -XX:MaxGCPauseMillis=200 -XX:+UseStringDeduplication -XX:+OptimizeStringConcat -XX:+UseCompressedOops -XX:+UseCompressedClassPointers -XX:MaxMetaspaceSize=128m -XX:ReservedCodeCacheSize=64m -XX:+ExitOnOutOfMemoryError -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/tmp -Djava.security.egd=file:/dev/./urandom -Dspring.jmx.enabled=false -Dspring.backgroundpreinitializer.ignore=true -Dserver.port=${PORT}"

# Log the port and memory settings (helpful for debugging)
echo "Starting application on port: ${PORT}"
echo "JVM Memory settings: Max Heap=512MB, Initial Heap=256MB, Max Metaspace=128MB"

# Run the Spring Boot application with dynamic port configuration and memory optimization
exec java ${JAVA_OPTS} -jar app.jar

