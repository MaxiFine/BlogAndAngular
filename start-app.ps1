# Quick Start Script for Blog Application
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "Blog Application - Quick Start" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

# Check if .env file exists
if (-Not (Test-Path ".env")) {
    Write-Host "WARNING: .env file not found!" -ForegroundColor Yellow
    Write-Host "Creating sample .env file..." -ForegroundColor Yellow
    @"
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/blogs
SPRING_DATASOURCE_USERNAME=postgres
SPRING_DATASOURCE_PASSWORD=password
"@ | Out-File -FilePath ".env" -Encoding UTF8
    Write-Host "Please update .env file with your database credentials" -ForegroundColor Yellow
    Write-Host ""
}

# Check if PostgreSQL is running
Write-Host "Checking PostgreSQL connection..." -ForegroundColor Green
$pgCheck = Test-NetConnection -ComputerName localhost -Port 5432 -InformationLevel Quiet -WarningAction SilentlyContinue -ErrorAction SilentlyContinue
if (-Not $pgCheck) {
    Write-Host "WARNING: Cannot connect to PostgreSQL on localhost:5432" -ForegroundColor Yellow
    Write-Host "Please ensure PostgreSQL is running before starting the application" -ForegroundColor Yellow
    Write-Host ""
}

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "Starting Backend (Spring Boot)" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan

# Start backend in a new window
Start-Process powershell -ArgumentList "-NoExit", "-Command", "Write-Host 'Starting Spring Boot Backend...' -ForegroundColor Green; .\mvnw.cmd spring-boot:run -Dspring-boot.run.profiles=dev"

Write-Host "Backend starting in new window..." -ForegroundColor Green
Write-Host "Waiting 15 seconds for backend to initialize..." -ForegroundColor Yellow
Start-Sleep -Seconds 15

Write-Host ""
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "Starting Frontend (Angular)" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan

# Check if node_modules exists in blogWeb
if (-Not (Test-Path "blogWeb\node_modules")) {
    Write-Host "Installing npm packages (this may take a few minutes)..." -ForegroundColor Yellow
    Set-Location blogWeb
    npm install
    Set-Location ..
}

# Start frontend in a new window
Start-Process powershell -ArgumentList "-NoExit", "-Command", "Write-Host 'Starting Angular Frontend...' -ForegroundColor Green; cd blogWeb; npm start"

Write-Host "Frontend starting in new window..." -ForegroundColor Green
Write-Host ""
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "Application Starting!" -ForegroundColor Green
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""
Write-Host "Backend:  http://localhost:8080/api/v1" -ForegroundColor Yellow
Write-Host "Frontend: http://localhost:4200" -ForegroundColor Yellow
Write-Host ""
Write-Host "The browser will open automatically once Angular is ready." -ForegroundColor Cyan
Write-Host "Press any key to exit this window..." -ForegroundColor Gray
$null = $Host.UI.RawUI.ReadKey("NoEcho,IncludeKeyDown")
