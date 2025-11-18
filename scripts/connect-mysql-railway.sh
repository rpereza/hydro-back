#!/bin/bash
# Bash script to connect to MySQL database in Railway
# Usage: ./scripts/connect-mysql-railway.sh

echo "=========================================="
echo "MySQL Railway Connection Helper"
echo "=========================================="
echo ""

# Prompt for MySQL credentials
echo "Enter your Railway MySQL credentials:"
echo ""

read -p "MySQL Host (e.g., containers-us-west-xxx.railway.app): " mysql_host
read -p "MySQL Port (default: 3306): " mysql_port
mysql_port=${mysql_port:-3306}
read -p "MySQL User: " mysql_user
read -s -p "MySQL Password: " mysql_password
echo ""
read -p "MySQL Database Name: " mysql_database

echo ""
echo "=========================================="
echo "Connection String:"
echo "=========================================="
echo "Host: $mysql_host"
echo "Port: $mysql_port"
echo "User: $mysql_user"
echo "Database: $mysql_database"
echo "=========================================="
echo ""

# Check if mysql command is available
if command -v mysql &> /dev/null; then
    echo "MySQL client found. Connecting..."
    echo ""
    
    mysql -h "$mysql_host" -P "$mysql_port" -u "$mysql_user" -p"$mysql_password" "$mysql_database"
else
    echo "MySQL command line client not found."
    echo ""
    echo "You can:"
    echo "1. Install MySQL client: sudo apt-get install mysql-client (Linux) or brew install mysql-client (Mac)"
    echo "2. Use MySQL Workbench: https://dev.mysql.com/downloads/workbench/"
    echo "3. Use DBeaver: https://dbeaver.io/download/"
    echo ""
    echo "Connection command:"
    echo "mysql -h $mysql_host -P $mysql_port -u $mysql_user -p $mysql_database"
fi

