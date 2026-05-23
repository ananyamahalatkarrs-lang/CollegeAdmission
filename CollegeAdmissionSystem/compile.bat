@echo off
REM College Admission System - Compilation Script
REM This script compiles all Java source files

echo.
echo ========================================
echo College Admission System - Build Script
echo ========================================
echo.

REM Set MongoDB Driver path
set MONGODB_JAR=lib\mongo-java-driver-3.12.10.jar

REM Check if lib directory exists
if not exist lib (
    echo Please download MongoDB Java Driver and place it in 'lib' folder
    echo Download from: https://oss.sonatype.org/content/repositories/releases/org/mongodb/mongo-java-driver/
    echo File: mongo-java-driver-3.12.10.jar
    echo.
    pause
    exit /b 1
)

REM Create bin directory if it doesn't exist
if not exist bin (
    mkdir bin
    echo Created bin directory
)

set CLASSPATH=bin;%MONGODB_JAR%

echo.
echo ========== Compiling Backend Models ==========
javac -d bin -cp %CLASSPATH% backend\src\com\collegeadmission\models\*.java
if errorlevel 1 (
    echo ERROR: Failed to compile models
    pause
    exit /b 1
)
echo Models compiled successfully

echo.
echo ========== Compiling Database Connection ==========
javac -d bin -cp %CLASSPATH% backend\src\com\collegeadmission\database\MongoDBConnection.java
if errorlevel 1 (
    echo ERROR: Failed to compile database connection
    pause
    exit /b 1
)
echo Database connection compiled successfully

echo.
echo ========== Compiling Services ==========
javac -d bin -cp %CLASSPATH% backend\src\com\collegeadmission\services\*.java
if errorlevel 1 (
    echo ERROR: Failed to compile services
    pause
    exit /b 1
)
echo Services compiled successfully

echo.
echo ========== Compiling Frontend Client ==========
javac -d bin -cp %CLASSPATH% frontend\src\com\collegeadmission\client\DataSyncManager.java
if errorlevel 1 (
    echo ERROR: Failed to compile frontend client
    pause
    exit /b 1
)
echo Frontend client compiled successfully

echo.
echo ========== Compiling UI Components ==========
javac -d bin -cp %CLASSPATH% frontend\src\com\collegeadmission\ui\*.java
if errorlevel 1 (
    echo ERROR: Failed to compile UI components
    pause
    exit /b 1
)
echo UI components compiled successfully

echo.
echo ========================================
echo Compilation completed successfully!
echo ========================================
echo.
echo To run the application:
echo   - Make sure MongoDB is running
echo   - Execute: run.bat
echo.
pause
