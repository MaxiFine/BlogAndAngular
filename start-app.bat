@echo off
echo ========================================
echo Blog Application - Quick Start
echo ========================================
echo.

REM Check if .env file exists
if not exist .env (
    echo WARNING: .env file not found!
    echo Creating sample .env file...
    (
        echo SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/blogs
        echo SPRING_DATASOURCE_USERNAME=postgres
        echo SPRING_DATASOURCE_PASSWORD=password
    ) > .env
    echo Please update .env file with your database credentials
    echo.
)

echo ========================================
echo Starting Backend (Spring Boot)
echo ========================================
start "Blog Backend" cmd /k "mvnw.cmd spring-boot:run -Dspring-boot.run.profiles=dev"

echo Backend starting in new window...
echo Waiting 15 seconds for backend to initialize...
timeout /t 15 /nobreak

echo.
echo ========================================
echo Starting Frontend (Angular)
echo ========================================

REM Check if node_modules exists
if not exist blogWeb\node_modules (
    echo Installing npm packages (this may take a few minutes)...
    cd blogWeb
    call npm install
    cd ..
)

start "Blog Frontend" cmd /k "cd blogWeb && npm start"

echo Frontend starting in new window...
echo.
echo ========================================
echo Application Starting!
echo ========================================
echo.
echo Backend:  http://localhost:8080/api/v1
echo Frontend: http://localhost:4200
echo.
echo The browser will open automatically once Angular is ready.
echo Press any key to exit...
pause > nul
