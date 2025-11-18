# PowerShell script to connect to MySQL database in Railway
# Usage: .\scripts\connect-mysql-railway.ps1

Write-Host "==========================================" -ForegroundColor Cyan
Write-Host "MySQL Railway Connection Helper" -ForegroundColor Yellow
Write-Host "==========================================" -ForegroundColor Cyan
Write-Host ""

# Prompt for MySQL credentials
Write-Host "Enter your Railway MySQL credentials:" -ForegroundColor Green
Write-Host ""

$mysqlHost = Read-Host "MySQL Host (e.g., containers-us-west-xxx.railway.app)"
$mysqlPort = Read-Host "MySQL Port (default: 3306)"
if ([string]::IsNullOrWhiteSpace($mysqlPort)) {
    $mysqlPort = "3306"
}
$mysqlUser = Read-Host "MySQL User"
$mysqlPassword = Read-Host "MySQL Password" -AsSecureString
$mysqlDatabase = Read-Host "MySQL Database Name"

# Convert secure string to plain text (needed for mysql command)
$BSTR = [System.Runtime.InteropServices.Marshal]::SecureStringToBSTR($mysqlPassword)
$plainPassword = [System.Runtime.InteropServices.Marshal]::PtrToStringAuto($BSTR)

Write-Host ""
Write-Host "==========================================" -ForegroundColor Cyan
Write-Host "Connection String:" -ForegroundColor Yellow
Write-Host "==========================================" -ForegroundColor Cyan
Write-Host "Host: $mysqlHost" -ForegroundColor White
Write-Host "Port: $mysqlPort" -ForegroundColor White
Write-Host "User: $mysqlUser" -ForegroundColor White
Write-Host "Database: $mysqlDatabase" -ForegroundColor White
Write-Host "==========================================" -ForegroundColor Cyan
Write-Host ""

# Check if mysql command is available
$mysqlPath = Get-Command mysql -ErrorAction SilentlyContinue

if ($mysqlPath) {
    Write-Host "MySQL client found. Connecting..." -ForegroundColor Green
    Write-Host ""
    
    # Build mysql command
    $mysqlCmd = "mysql -h $mysqlHost -P $mysqlPort -u $mysqlUser -p`"$plainPassword`" $mysqlDatabase"
    
    Write-Host "To connect, run:" -ForegroundColor Yellow
    Write-Host $mysqlCmd -ForegroundColor White
    Write-Host ""
    
    $connect = Read-Host "Connect now? (Y/N)"
    if ($connect -eq "Y" -or $connect -eq "y") {
        & mysql -h $mysqlHost -P $mysqlPort -u $mysqlUser -p"$plainPassword" $mysqlDatabase
    }
} else {
    Write-Host "MySQL command line client not found." -ForegroundColor Yellow
    Write-Host ""
    Write-Host "You can:" -ForegroundColor Cyan
    Write-Host "1. Install MySQL client: https://dev.mysql.com/downloads/mysql/" -ForegroundColor White
    Write-Host "2. Use MySQL Workbench: https://dev.mysql.com/downloads/workbench/" -ForegroundColor White
    Write-Host "3. Use DBeaver: https://dbeaver.io/download/" -ForegroundColor White
    Write-Host ""
    Write-Host "Connection details saved above for manual use." -ForegroundColor Green
}

# Clear password from memory
$plainPassword = $null
$BSTR = $null

