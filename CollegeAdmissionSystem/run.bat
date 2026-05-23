@echo off
REM College Admission System - Execution Script

echo.
echo ========================================
echo College Admission System
echo Starting Application...
echo ========================================
echo.

REM Check if bin directory exists
if not exist bin (
    echo ERROR: Bin directory not found!
    echo Please run compile.bat first
    pause
    exit /b 1
)

REM Check if MongoDB driver exists
if not exist lib\mongo-java-driver-3.12.10.jar (
    echo ERROR: MongoDB driver not found!
    echo Please download and place mongo-java-driver-3.12.10.jar in 'lib' folder
    pause
    exit /b 1
)

REM Set classpath
set CLASSPATH=bin;lib\mongo-java-driver-3.12.10.jar

echo Starting Java application...
echo.

REM Run the main application
java -cp %CLASSPATH% com.collegeadmission.ui.MainFrame

if errorlevel 1 (
    echo.
    echo ERROR: Failed to start application
    echo Make sure MongoDB is running on localhost:27017
    pause
)
