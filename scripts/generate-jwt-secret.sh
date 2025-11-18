#!/bin/bash
# Script to generate a secure JWT secret for Railway deployment

echo "Generating secure JWT secret..."
SECRET=$(openssl rand -base64 64 | tr -d '\n')
echo ""
echo "=========================================="
echo "JWT_SECRET for Railway:"
echo "=========================================="
echo "$SECRET"
echo "=========================================="
echo ""
echo "Copy this value and paste it into Railway environment variables as JWT_SECRET"
echo ""

