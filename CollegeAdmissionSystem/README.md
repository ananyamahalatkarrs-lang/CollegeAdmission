# College Admission System

A comprehensive College Admission Management System built with **Java Swing GUI** and **MongoDB** database.

## Features

### Core Modules
1. **Student Management** - Register and manage student information
2. **Program Management** - Create and manage academic programs
3. **Application Processing** - Handle student applications for programs
4. **Payment Management** - Track and process application fees
5. **Merit List** - Generate and publish merit-based rankings
6. **Hostel Allocation** - Manage hostel assignments (Dayscholar/Hostel)
7. **Admin Panel** - Administrative controls and oversight

## Project Structure

```
CollegeAdmissionSystem/
├── backend/
│   └── src/com/collegeadmission/
│       ├── models/             # Data models
│       │   ├── Student.java
│       │   ├── Application.java
│       │   ├── Program.java
│       │   ├── Payment.java
│       │   ├── MeritList.java
│       │   ├── HostelStatus.java
│       │   └── Admin.java
│       ├── database/           # Database connections
│       │   └── MongoDBConnection.java
│       └── services/           # Business logic
│           ├── StudentService.java
│           ├── ApplicationService.java
│           ├── ProgramService.java
│           ├── PaymentService.java
│           └── MeritListService.java
├── frontend/
│   └── src/com/collegeadmission/
│       ├── ui/                 # GUI Components
│       │   ├── MainFrame.java
│       │   ├── StudentPanel.java
│       │   ├── ApplicationPanel.java
│       │   ├── ProgramPanel.java
│       │   └── AdminPanel.java
│       └── client/             # Client utilities
│           └── DataSyncManager.java
└── README.md

```

## Prerequisites

- **Java JDK 8+**
- **MongoDB 4.0+** (Already installed on your system)
- **IDE** (Eclipse, IntelliJ IDEA, or VS Code)

## Database Setup

### MongoDB Collections

The system automatically creates the following collections:

1. **students** - Stores student information
2. **programs** - Academic program details
3. **applications** - Student applications
4. **payments** - Payment records
5. **merit_lists** - Merit rankings
6. **hostel_status** - Hostel allocation information
7. **admin** - Administrator accounts

### MongoDB Connection Details

```
Host: localhost
Port: 27017
Database: college_admission
```

## Installation & Setup

### Step 1: Download MongoDB Driver

Download MongoDB Java driver from: https://oss.sonatype.org/content/repositories/releases/org/mongodb/mongo-java-driver/

Recommended version: `mongo-java-driver-3.12.10.jar`

### Step 2: Compile the Project

```bash
# Navigate to project directory
cd d:\DBMS\CollegeAdmissionSystem

# Add MongoDB driver to classpath
set CLASSPATH=%CLASSPATH%;path_to_mongo_driver.jar

# Compile backend
javac -d bin backend/src/com/collegeadmission/models/*.java
javac -d bin -cp bin backend/src/com/collegeadmission/database/*.java
javac -d bin -cp bin backend/src/com/collegeadmission/services/*.java

# Compile frontend
javac -d bin -cp bin frontend/src/com/collegeadmission/client/*.java
javac -d bin -cp bin frontend/src/com/collegeadmission/ui/*.java

```

### Step 3: Run the Application

```bash
# Ensure MongoDB is running
mongod

# Run the application
java -cp bin;path_to_mongo_driver.jar com.collegeadmission.ui.MainFrame
```

## Features in Detail

### 1. Student Registration
- Register new students with personal information
- Capture 10th and 12th-grade marks
- Store category information (General, OBC, SC, ST)
- Track registration date and status

### 2. Programs Management
- Create new academic programs
- Set program codes and departments
- Configure seat availability and cutoff marks
- Set program duration
- Add program descriptions

### 3. Application Processing
- Students can apply to multiple programs
- Calculate merit scores automatically
- Track application status (Pending, Approved, Rejected)
- Maintain application timeline

### 4. Payment Tracking
- Record application fees
- Support multiple payment methods
- Generate transaction IDs
- Track payment status

### 5. Merit List Generation
- Rank students based on merit
- Publish merit lists by program
- Track allocation status
- Generate rankings automatically

### 6. Hostel Management
- Allocate hostel/dayscholar status
- Track room assignments
- Manage hostel preferences
- Record allocation status

### 7. Admin Dashboard
- Overview of all operations
- System status monitoring
- User and role management
- Audit trail access

## Data Flow

```
User Input (GUI)
    ↓
DataSyncManager (Frontend Client)
    ↓
Service Layer (Business Logic)
    ↓
MongoDBConnection (Database Access)
    ↓
MongoDB Collections
    ↓
Real-time Synchronization Back to UI
```

## MongoDB Schema

### Student Collection
```javascript
{
  _id: ObjectId,
  firstName: String,
  lastName: String,
  email: String,
  phoneNumber: String,
  address: String,
  dateOfBirth: Date,
  gender: String,
  tenthMarks: Number,
  twelfthMarks: Number,
  category: String,
  registrationDate: Date,
  status: String
}
```

### Application Collection
```javascript
{
  _id: ObjectId,
  studentId: ObjectId,
  programId: ObjectId,
  applicationDate: Date,
  status: String,
  merit: Number,
  lastModified: Date
}
```

### Program Collection
```javascript
{
  _id: ObjectId,
  programName: String,
  programCode: String,
  department: String,
  totalSeats: Number,
  availableSeats: Number,
  minCutoff: Number,
  description: String,
  duration: Number
}
```

### Payment Collection
```javascript
{
  _id: ObjectId,
  studentId: ObjectId,
  applicationId: ObjectId,
  amount: Number,
  paymentDate: Date,
  paymentMethod: String,
  transactionId: String,
  status: String
}
```

### Merit List Collection
```javascript
{
  _id: ObjectId,
  programId: ObjectId,
  studentId: ObjectId,
  rank: Number,
  meritScore: Number,
  status: String,
  publishedDate: Date,
  allocationStatus: String
}
```

### Hostel Status Collection
```javascript
{
  _id: ObjectId,
  studentId: ObjectId,
  hostelType: String,
  roomNumber: String,
  allocatedHostel: String,
  status: String,
  preferences: String
}
```

## Usage Guide

### Adding a Student
1. Click on "Students" tab
2. Fill in all student information
3. Click "Add Student" button
4. Confirm the operation in the success dialog
5. Data automatically syncs to MongoDB

### Creating a Program
1. Navigate to "Programs" tab
2. Enter program details (name, code, seats, cutoff)
3. Click "Add Program"
4. Program is saved to database

### Processing Applications
1. Go to "Applications" tab
2. Select student and program from dropdowns
3. Enter merit score
4. Click "Apply Now"
5. Track status and update as needed

### Managing Hostel Allocation
- Students can indicate hostel/dayscholar preference
- Admin can allocate rooms from available inventory
- System tracks allocation history

## Troubleshooting

### MongoDB Connection Issues
```
Error: Failed to initialize MongoDB connection
Solution: 
- Ensure MongoDB service is running (mongod.exe)
- Check port 27017 is accessible
- Verify MongoDB is installed correctly
```

### ClassNotFound Exception
```
Error: ClassNotFoundException for MongoDB classes
Solution:
- Add MongoDB driver JAR to classpath
- Verify CLASSPATH environment variable
```

### Port Already in Use
```
Error: Address already in use
Solution:
- Check if MongoDB is already running on port 27017
- Use: netstat -ano | findstr 27017
- Kill process or change MongoDB port
```

## Future Enhancements

1. **Email Notifications** - Send updates to students
2. **Document Upload** - Accept certificates and documents
3. **Interview Management** - Schedule and track interviews
4. **Reporting** - Generate comprehensive reports
5. **Multi-language Support** - Support for regional languages
6. **Mobile App** - Android/iOS companion app
7. **Email Integration** - Automated email confirmations
8. **Advanced Analytics** - Dashboard with statistics

## Technical Stack

- **Frontend**: Java Swing (GUI Framework)
- **Backend**: Java (Business Logic)
- **Database**: MongoDB (NoSQL Database)
- **Language**: Java 8+
- **Architecture**: 3-Tier (Presentation, Business, Data)

## Performance Considerations

- **Indexing**: Create indexes on frequently queried fields
- **Connection Pooling**: Implemented in MongoDBConnection
- **Async Operations**: Data sync runs on separate threads
- **Lazy Loading**: Collections loaded on demand

## Security Notes

- Implement password hashing for admin accounts
- Add user authentication/authorization
- Validate all input fields
- Use environment variables for sensitive data
- Enable MongoDB authentication

## License

This project is for educational purposes.

## Support

For issues or questions:
1. Check MongoDB connection
2. Verify all JAR files are in classpath
3. Review console error messages
4. Ensure Java version is 8 or higher

---

**Version**: 1.0  
**Last Updated**: 2026  
**Developer**: DBMS Project Team
