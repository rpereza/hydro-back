# PowerShell script to generate a secure JWT secret for Railway deployment

Write-Host "Generating secure JWT secret..." -ForegroundColor Green
Write-Host ""

# Generate random bytes and convert to base64
$bytes = New-Object byte[] 64
[System.Security.Cryptography.RandomNumberGenerator]::Fill($bytes)
$secret = [Convert]::ToBase64String($bytes)

Write-Host "==========================================" -ForegroundColor Cyan
Write-Host "JWT_SECRET for Railway:" -ForegroundColor Yellow
Write-Host "==========================================" -ForegroundColor Cyan
Write-Host $secret -ForegroundColor White
Write-Host "==========================================" -ForegroundColor Cyan
Write-Host ""
Write-Host "Copy this value and paste it into Railway environment variables as JWT_SECRET" -ForegroundColor Green
Write-Host ""

