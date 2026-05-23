# College Admission System - Compilation and Execution Guide

## Step-by-Step Setup Instructions for Windows

### Prerequisites
1. Java JDK 8 or higher installed
2. MongoDB installed and running
3. MongoDB Java Driver JAR file

### Step 1: Download MongoDB Java Driver

```bash
# Download from official source or Maven repository
# File: mongo-java-driver-3.12.10.jar
# Place in: d:\DBMS\CollegeAdmissionSystem\lib\
```

### Step 2: Create Directory Structure

```bash
cd d:\DBMS\CollegeAdmissionSystem
mkdir bin
mkdir lib
```

### Step 3: Compile Backend Code

```bash
# Set CLASSPATH
set CLASSPATH=d:\DBMS\CollegeAdmissionSystem\lib\mongo-java-driver-3.12.10.jar

# Compile models
javac -d bin -cp %CLASSPATH% backend\src\com\collegeadmission\models\*.java

# Compile database connection
javac -d bin -cp %CLASSPATH% backend\src\com\collegeadmission\database\MongoDBConnection.java

# Compile services
javac -d bin -cp %CLASSPATH% backend\src\com\collegeadmission\services\*.java
```

### Step 4: Compile Frontend Code

```bash
# Compile client utilities
javac -d bin -cp %CLASSPATH% frontend\src\com\collegeadmission\client\DataSyncManager.java

# Compile UI components
javac -d bin -cp %CLASSPATH% frontend\src\com\collegeadmission\ui\*.java
```

### Step 5: Start MongoDB Service

```bash
# Ensure MongoDB is running
# Windows Service or run:
mongod --dbpath "C:\data\db"
```

### Step 6: Run the Application

```bash
# Set classpath with MongoDB driver
set CLASSPATH=bin;lib\mongo-java-driver-3.12.10.jar

# Run main application
java -cp %CLASSPATH% com.collegeadmission.ui.MainFrame
```

## Batch Script for Compilation (compile.bat)

Create a file named `compile.bat` in the root directory:

```batch
@echo off
REM College Admission System - Compilation Script
REM Author: DBMS Project Team

echo Compiling College Admission System...

set CLASSPATH=d:\DBMS\CollegeAdmissionSystem\lib\mongo-java-driver-3.12.10.jar

REM Create directories if they don't exist
if not exist bin mkdir bin
if not exist lib mkdir lib

echo.
echo Compiling Backend Models...
javac -d bin -cp %CLASSPATH% backend\src\com\collegeadmission\models\*.java

echo Compiling Database Connection...
javac -d bin -cp %CLASSPATH% backend\src\com\collegeadmission\database\MongoDBConnection.java

echo Compiling Services...
javac -d bin -cp %CLASSPATH% backend\src\com\collegeadmission\services\*.java

echo.
echo Compiling Frontend Client...
javac -d bin -cp %CLASSPATH% frontend\src\com\collegeadmission\client\DataSyncManager.java

echo Compiling UI Components...
javac -d bin -cp %CLASSPATH% frontend\src\com\collegeadmission\ui\*.java

echo.
echo Compilation completed successfully!
echo To run the application, execute: run.bat
```

## Batch Script for Execution (run.bat)

Create a file named `run.bat`:

```batch
@echo off
REM College Admission System - Execution Script

echo Starting College Admission System...

set CLASSPATH=bin;lib\mongo-java-driver-3.12.10.jar

java -cp %CLASSPATH% com.collegeadmission.ui.MainFrame

pause
```

## Using in IDE (Eclipse/IntelliJ)

### Eclipse:
1. Create New Java Project
2. Set Project Structure
3. Right-click project > Build Path > Configure Build Path
4. Add External JARs (MongoDB driver)
5. Create source folders: `backend/src` and `frontend/src`
6. Copy all source files
7. Run as Java Application

### IntelliJ IDEA:
1. File > New > Project from Existing Sources
2. Point to CollegeAdmissionSystem directory
3. Set up sources root for each module
4. Add MongoDB JAR as library dependency
5. Run > Run Application

## Troubleshooting Compilation Issues

### Issue: "javac: command not found"
```
Solution: Add Java bin directory to PATH
C:\Program Files\Java\jdk1.8.0_XXX\bin
```

### Issue: "error: package org.bson does not exist"
```
Solution: Ensure MongoDB driver is in classpath
Check CLASSPATH environment variable
```

### Issue: "error: cannot find symbol"
```
Solution: 
- Verify all source files are in correct locations
- Check for typos in package names
- Recompile in order: models → database → services → ui
```

## Database Initialization

On first run, the system will:
1. Check MongoDB connection
2. Create database "college_admission" if not exists
3. Create all required collections
4. Initialize with empty collections

## Testing the System

### Test Student Registration:
1. Fill in all student fields
2. Click "Add Student"
3. Check console for confirmation
4. Verify data in MongoDB:
   ```
   mongo
   > use college_admission
   > db.students.find()
   ```

### Test Program Creation:
1. Go to Programs tab
2. Add test program
3. Verify in database

### Test Data Sync:
1. Add data via GUI
2. Check MongoDB immediately
3. Refresh GUI to see real-time updates

## Performance Tips

1. **Indexing**: Add indexes for frequently searched fields
   ```
   db.students.createIndex({email: 1})
   db.applications.createIndex({studentId: 1, programId: 1})
   ```

2. **Connection Pooling**: Already implemented in MongoDBConnection

3. **Lazy Loading**: UI loads data only when needed

## Backup MongoDB Data

```bash
# Backup database
mongodump --db college_admission --out "C:\backup\college_admission_backup"

# Restore database
mongorestore --db college_admission "C:\backup\college_admission_backup\college_admission"
```

## Environment Variables (Optional)

Set permanent environment variables:

```
JAVA_HOME=C:\Program Files\Java\jdk1.8.0_XXX
MONGODB_PATH=C:\Program Files\MongoDB\Server\4.4\bin
PATH=%JAVA_HOME%\bin;%MONGODB_PATH%;%PATH%
```

---

For additional help, refer to README.md file.
