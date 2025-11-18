# PowerShell script to generate a secure JWT secret for Railway deployment
# Compatible with PowerShell 5.1+ and older .NET Framework versions

Write-Host "Generating secure JWT secret..." -ForegroundColor Green
Write-Host ""

# Generate random bytes using RNGCryptoServiceProvider (compatible with older PowerShell versions)
$rng = New-Object System.Security.Cryptography.RNGCryptoServiceProvider
$bytes = New-Object byte[] 64
$rng.GetBytes($bytes)
$secret = [Convert]::ToBase64String($bytes)

# Clean up
$rng.Dispose()

Write-Host "==========================================" -ForegroundColor Cyan
Write-Host "JWT_SECRET for Railway:" -ForegroundColor Yellow
Write-Host "==========================================" -ForegroundColor Cyan
Write-Host $secret -ForegroundColor White
Write-Host "==========================================" -ForegroundColor Cyan
Write-Host ""
Write-Host "Copy this value and paste it into Railway environment variables as JWT_SECRET" -ForegroundColor Green
Write-Host ""
Write-Host "Length: $($secret.Length) characters" -ForegroundColor Gray
Write-Host ""

