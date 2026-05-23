# College Admission System - Configuration Guide

## Environment Variables

Add these to your system environment for easier execution:

```
JAVA_HOME=C:\Program Files\Java\jdk1.8.0_281
MONGODB_PATH=C:\Program Files\MongoDB\Server\4.4
COLLEGE_ADMISSION_HOME=d:\DBMS\CollegeAdmissionSystem
```

## MongoDB Configuration

### Connection Details
```
Host: localhost
Port: 27017
Database: college_admission
Authentication: None (for development)
```

### For Production Setup

1. **Enable Authentication**:
```javascript
use admin;
db.createUser({
  user: "college_admin",
  pwd: "secure_password",
  roles: ["dbOwner"]
});
```

2. **Connect with Authentication**:
```java
MongoCredential credential = MongoCredential.createCredential(
    "college_admin",
    "college_admission",
    "secure_password".toCharArray()
);
```

## Java Compiler Options

### For older systems:
```bash
javac -target 1.8 -source 1.8 -d bin -cp %CLASSPATH% file.java
```

### For 64-bit systems:
```bash
java -Xmx1024m -cp %CLASSPATH% com.collegeadmission.ui.MainFrame
```

## IDE Configuration

### Eclipse Setup:
1. Project > Properties > Java Build Path
2. Add External Archive: mongo-java-driver JAR
3. Source > Create new Source Folder
4. Set Output Folder: bin/

### IntelliJ IDEA:
1. File > Project Structure > Libraries
2. Add JAR: MongoDB driver
3. Set Source Roots: frontend/src, backend/src
4. Set Output path: bin/

## Logging Configuration

To enable detailed logging, create `log4j.properties`:

```properties
log4j.rootLogger=INFO, file

log4j.appender.file=org.apache.log4j.FileAppender
log4j.appender.file.File=logs/application.log
log4j.appender.file.layout=org.apache.log4j.PatternLayout
log4j.appender.file.layout.ConversionPattern=%d{ABSOLUTE} %5p %c{1}:%L - %m%n

log4j.logger.com.collegeadmission=DEBUG
```

## Performance Tuning

### MongoDB Performance:
```javascript
// Create indexes for faster queries
db.students.createIndex({email: 1});
db.applications.createIndex({studentId: 1, status: 1});
db.merit_lists.createIndex({programId: 1, rank: 1});
```

### Java Heap Size:
```bash
REM For systems with sufficient memory
java -Xms512m -Xmx1024m -cp %CLASSPATH% com.collegeadmission.ui.MainFrame
```

## Backup Configuration

### Automated Backup Script:
```batch
@echo off
setlocal enabledelayedexpansion

REM Backup MongoDB data
set TIMESTAMP=%date:~-4%%date:~-10,2%%date:~-7,2%_%time:~0,2%%time:~3,2%%time:~6,2%
set BACKUP_DIR=backups\college_admission_!TIMESTAMP!

mongodump --db college_admission --out "%BACKUP_DIR%"

echo Backup completed to: %BACKUP_DIR%
```

## Deployment Checklist

- [ ] Java JDK installed and in PATH
- [ ] MongoDB installed and running
- [ ] MongoDB Java Driver in lib/ folder
- [ ] compile.bat executed successfully
- [ ] All source files compiled to bin/
- [ ] MongoDB connection tested
- [ ] Sample data created
- [ ] All tabs functioning
- [ ] CRUD operations working
- [ ] Data persisting in MongoDB

## Version Compatibility Matrix

| Component | Version | Status |
|-----------|---------|--------|
| Java | 8+ | ✓ Tested |
| MongoDB | 4.0+ | ✓ Tested |
| MongoDB Driver | 3.12.10 | ✓ Recommended |
| Windows | 7+ | ✓ Compatible |
| Eclipse | 2020+ | ✓ Compatible |
| IntelliJ | 2020+ | ✓ Compatible |

## Troubleshooting Configurations

### Low Memory Issues:
```bash
java -Xms256m -Xmx512m -cp %CLASSPATH% com.collegeadmission.ui.MainFrame
```

### Connection Timeout:
```java
// In MongoDBConnection.java
MongoClientOptions options = MongoClientOptions.builder()
    .connectTimeout(5000)
    .socketTimeout(5000)
    .build();
```

### Port Already in Use:
```bash
REM Find process using port 27017
netstat -ano | findstr 27017

REM Kill process
taskkill /PID <PID> /F
```

---

For more information, see README.md and SETUP.md
