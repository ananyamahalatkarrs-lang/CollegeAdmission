# College Admission System - MongoDB Query Reference

## Connection & Database Setup

### Connect to MongoDB
```bash
mongosh
# or older version:
mongo
```

### Select Database
```javascript
use college_admission
```

### Show Collections
```javascript
show collections
```

## STUDENTS Collection

### Insert New Student
```javascript
db.students.insertOne({
  firstName: "Raj",
  lastName: "Kumar",
  email: "raj.kumar@email.com",
  phoneNumber: "9876543210",
  address: "123 Main Street",
  dateOfBirth: new Date("2004-05-15"),
  gender: "Male",
  tenthMarks: 85.5,
  twelfthMarks: 88.0,
  category: "General",
  registrationDate: new Date(),
  status: "Active"
})
```

### Find All Students
```javascript
db.students.find()

# Pretty print
db.students.find().pretty()
```

### Find Student by Email
```javascript
db.students.findOne({email: "raj.kumar@email.com"})
```

### Find Students by Category
```javascript
db.students.find({category: "SC"})
```

### Find High Performers (12th marks > 85)
```javascript
db.students.find({"twelfthMarks": {$gt: 85}})
```

### Update Student Information
```javascript
db.students.updateOne(
  {email: "raj.kumar@email.com"},
  {$set: {
    phoneNumber: "9123456789",
    address: "456 Oak Avenue"
  }}
)
```

### Update Multiple Students Status
```javascript
db.students.updateMany(
  {category: "SC"},
  {$set: {status: "Active"}}
)
```

### Delete Student
```javascript
db.students.deleteOne({email: "raj.kumar@email.com"})
```

### Count Students
```javascript
db.students.countDocuments()
```

### Get Student Statistics
```javascript
db.students.aggregate([
  {$group: {
    _id: "$category",
    count: {$sum: 1},
    avgMarks: {$avg: "$twelfthMarks"}
  }}
])
```

## PROGRAMS Collection

### Insert New Program
```javascript
db.programs.insertOne({
  programName: "Bachelor of Engineering - Computer Science",
  programCode: "CSE101",
  department: "Engineering",
  totalSeats: 120,
  availableSeats: 120,
  minCutoff: 75.0,
  description: "4-year B.Tech program in Computer Science",
  duration: 4
})
```

### Find All Programs
```javascript
db.programs.find()
```

### Find Program by Code
```javascript
db.programs.findOne({programCode: "CSE101"})
```

### Find Programs with Available Seats
```javascript
db.programs.find({availableSeats: {$gt: 0}})
```

### Update Program Seats
```javascript
db.programs.updateOne(
  {programCode: "CSE101"},
  {$set: {availableSeats: 115}}
)
```

### Reduce Available Seats (when student is admitted)
```javascript
db.programs.updateOne(
  {programCode: "CSE101"},
  {$inc: {availableSeats: -1}}
)
```

## APPLICATIONS Collection

### Insert New Application
```javascript
db.applications.insertOne({
  studentId: ObjectId("student_id_here"),
  programId: ObjectId("program_id_here"),
  applicationDate: new Date(),
  status: "Pending",
  merit: 88.5,
  lastModified: new Date()
})
```

### Find All Applications
```javascript
db.applications.find()
```

### Find Applications by Student
```javascript
db.applications.find({studentId: ObjectId("student_id")})
```

### Find Applications by Status
```javascript
db.applications.find({status: "Pending"})
```

### Get All Approved Applications
```javascript
db.applications.find({status: "Approved"})
```

### Update Application Status
```javascript
db.applications.updateOne(
  {_id: ObjectId("application_id")},
  {$set: {
    status: "Approved",
    lastModified: new Date()
  }}
)
```

### Count Pending Applications
```javascript
db.applications.countDocuments({status: "Pending"})
```

### Find Top Merit Students for Program
```javascript
db.applications.find({programId: ObjectId("program_id")})
  .sort({merit: -1})
  .limit(10)
```

## PAYMENTS Collection

### Insert Payment Record
```javascript
db.payments.insertOne({
  studentId: ObjectId("student_id"),
  applicationId: ObjectId("application_id"),
  amount: 500,
  paymentDate: new Date(),
  paymentMethod: "Credit Card",
  transactionId: "TXN123456",
  status: "Completed"
})
```

### Find Payments by Student
```javascript
db.payments.find({studentId: ObjectId("student_id")})
```

### Find Completed Payments
```javascript
db.payments.find({status: "Completed"})
```

### Calculate Total Revenue
```javascript
db.payments.aggregate([
  {$match: {status: "Completed"}},
  {$group: {
    _id: null,
    totalRevenue: {$sum: "$amount"}
  }}
])
```

### Find Payments by Method
```javascript
db.payments.find({paymentMethod: "Credit Card"})
```

## MERIT_LISTS Collection

### Insert Merit Entry
```javascript
db.merit_lists.insertOne({
  programId: ObjectId("program_id"),
  studentId: ObjectId("student_id"),
  rank: 1,
  meritScore: 92.5,
  status: "Published",
  publishedDate: new Date(),
  allocationStatus: "Not Allocated"
})
```

### Get Merit List for Program (Ranked)
```javascript
db.merit_lists.find({programId: ObjectId("program_id")})
  .sort({rank: 1})
```

### Find Top 10 Merit Students
```javascript
db.merit_lists.find()
  .sort({meritScore: -1})
  .limit(10)
```

### Update Allocation Status
```javascript
db.merit_lists.updateOne(
  {_id: ObjectId("merit_id")},
  {$set: {allocationStatus: "Allocated"}}
)
```

### Count Allocated Seats
```javascript
db.merit_lists.countDocuments({allocationStatus: "Allocated"})
```

## HOSTEL_STATUS Collection

### Insert Hostel Record
```javascript
db.hostel_status.insertOne({
  studentId: ObjectId("student_id"),
  hostelType: "Boys Hostel",
  roomNumber: null,
  allocatedHostel: null,
  status: "Pending",
  preferences: "Ground Floor"
})
```

### Find Hostel Requests
```javascript
db.hostel_status.find()
```

### Find Pending Hostel Allocations
```javascript
db.hostel_status.find({status: "Pending"})
```

### Update Hostel Allocation
```javascript
db.hostel_status.updateOne(
  {studentId: ObjectId("student_id")},
  {$set: {
    roomNumber: "B-101",
    allocatedHostel: "Boys Hostel A",
    status: "Allocated"
  }}
)
```

### Get Hostel Statistics
```javascript
db.hostel_status.aggregate([
  {$group: {
    _id: "$hostelType",
    count: {$sum: 1}
  }}
])
```

## ADMIN Collection

### Create Admin User
```javascript
db.admin.insertOne({
  username: "admin",
  password: "hashedPassword",
  email: "admin@college.edu",
  fullName: "System Administrator",
  role: "Super Admin",
  department: "Administration",
  createdDate: new Date(),
  status: "Active",
  permissions: "all"
})
```

### Find Admin by Username
```javascript
db.admin.findOne({username: "admin"})
```

## AGGREGATION QUERIES

### Get Application Statistics
```javascript
db.applications.aggregate([
  {$group: {
    _id: "$status",
    count: {$sum: 1},
    avgMerit: {$avg: "$merit"}
  }},
  {$sort: {count: -1}}
])
```

### Get Program-wise Applications
```javascript
db.applications.aggregate([
  {$group: {
    _id: "$programId",
    totalApplications: {$sum: 1},
    approvedCount: {$sum: {$cond: [{$eq: ["$status", "Approved"]}, 1, 0]}}
  }}
])
```

### Get Students with Multiple Applications
```javascript
db.applications.aggregate([
  {$group: {
    _id: "$studentId",
    applicationCount: {$sum: 1}
  }},
  {$match: {applicationCount: {$gt: 1}}}
])
```

### Dashboard Summary
```javascript
db.applications.aggregate([
  {$facet: {
    byStatus: [
      {$group: {_id: "$status", count: {$sum: 1}}}
    ],
    topMerit: [
      {$sort: {merit: -1}},
      {$limit: 5}
    ],
    totalApplications: [
      {$count: "total"}
    ]
  }}
])
```

## INDEXING FOR PERFORMANCE

### Create Indexes
```javascript
// Index for faster student lookups by email
db.students.createIndex({email: 1}, {unique: true})

// Index for application searches
db.applications.createIndex({studentId: 1})
db.applications.createIndex({programId: 1})
db.applications.createIndex({status: 1})

// Index for merit list
db.merit_lists.createIndex({programId: 1, rank: 1})

// Index for payments
db.payments.createIndex({studentId: 1, status: 1})

// Compound index for common queries
db.applications.createIndex({studentId: 1, programId: 1, status: 1})
```

### View Indexes
```javascript
db.students.getIndexes()
```

### Drop Index
```javascript
db.students.dropIndex("email_1")
```

## BACKUP & RESTORE

### Backup Database
```bash
mongodump --db college_admission --out "backup_folder"
```

### Restore Database
```bash
mongorestore --db college_admission "backup_folder/college_admission"
```

## USEFUL QUERIES

### Find Students Eligible for Merit List (marks > 75)
```javascript
db.students.find({"twelfthMarks": {$gte: 75}})
```

### Get Pending and Rejected Applications
```javascript
db.applications.find({
  status: {$in: ["Pending", "Rejected"]}
})
```

### Find Applications from Last 7 Days
```javascript
db.applications.find({
  applicationDate: {
    $gte: new Date(new Date().getTime() - 7*24*60*60*1000)
  }
})
```

### Get Revenue by Month
```javascript
db.payments.aggregate([
  {$match: {status: "Completed"}},
  {$group: {
    _id: {$month: "$paymentDate"},
    monthlyRevenue: {$sum: "$amount"}
  }}
])
```

---

**Note**: Replace `ObjectId("...")` with actual MongoDB ObjectIDs from your database.
For more MongoDB documentation, visit: https://docs.mongodb.com/manual/
