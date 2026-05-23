@echo off
REM College Admission System - MongoDB Setup Script

echo.
echo ========================================
echo College Admission System - MongoDB Setup
echo ========================================
echo.

REM Check if MongoDB is installed
where mongod >nul 2>nul
if %errorlevel% neq 0 (
    echo ERROR: MongoDB is not installed or not in PATH
    echo Please install MongoDB from: https://www.mongodb.com/try/download/community
    pause
    exit /b 1
)

echo Checking MongoDB connection...

REM Try to connect to MongoDB
mongosh --eval "db.version()" >nul 2>nul
if %errorlevel% equ 0 (
    echo MongoDB is already running!
) else (
    echo MongoDB is not running. Starting MongoDB service...
    REM Start MongoDB (requires MongoDB to be installed as service)
    net start MongoDB
    if %errorlevel% equ 0 (
        echo MongoDB started successfully
        timeout /t 3
    ) else (
        echo Could not start MongoDB service
        echo Please start MongoDB manually from Services or run:
        echo   mongod --dbpath "path\to\data\db"
        pause
        exit /b 1
    )
)

echo.
echo Setting up College Admission database...

REM Initialize database collections
mongosh <<EOF
use college_admission;
db.createCollection('students');
db.createCollection('programs');
db.createCollection('applications');
db.createCollection('payments');
db.createCollection('merit_lists');
db.createCollection('hostel_status');
db.createCollection('admin');

print('Database and collections created successfully!');
EOF

echo.
echo ========================================
echo MongoDB setup completed!
echo ========================================
echo.
echo You can now run: compile.bat and then run.bat
echo.
pause
