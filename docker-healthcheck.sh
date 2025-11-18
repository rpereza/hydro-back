#!/bin/sh
# Health check script that uses dynamic PORT from environment
PORT=${PORT:-8080}
curl -f http://localhost:${PORT}/actuator/health || exit 1

