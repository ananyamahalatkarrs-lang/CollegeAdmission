package com.collegeadmission.database;

import com.mongodb.client.*;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.Indexes;
import com.mongodb.client.model.Updates;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientSettings;
import com.mongodb.ServerAddress;
import com.mongodb.connection.ClusterSettings;
import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import java.util.*;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

public class MongoDBConnection {
    private static MongoClient mongoClient;
    private static MongoDatabase database;
    private static final String DATABASE_NAME = "college_admission";
    private static final String HOST = "localhost";
    private static final int PORT = 27017;

    static {
        try {
            initializeConnection();
        } catch (Exception e) {
            System.err.println("Failed to initialize MongoDB connection: " + e.getMessage());
        }
    }

    public static void initializeConnection() {
        try {
            // Create MongoClient
            mongoClient = new MongoClient(HOST, PORT);
            
            // Get database
            database = mongoClient.getDatabase(DATABASE_NAME);
            
            // Create collections if they don't exist
            createCollections();
            
            System.out.println("MongoDB connection established successfully!");
        } catch (Exception e) {
            System.err.println("Error connecting to MongoDB: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void createCollections() {
        try {
            MongoIterable<String> collectionNames = database.listCollectionNames();
            List<String> collections = new ArrayList<>();
            for (String name : collectionNames) {
                collections.add(name);
            }

            // Create collections if they don't exist
            if (!collections.contains("students")) {
                database.createCollection("students");
                System.out.println("Created 'students' collection");
            }
            if (!collections.contains("courses")) {
                database.createCollection("courses");
                System.out.println("Created 'courses' collection");
            }
            if (!collections.contains("applications")) {
                database.createCollection("applications");
                System.out.println("Created 'applications' collection");
            }
            if (!collections.contains("payments")) {
                database.createCollection("payments");
                System.out.println("Created 'payments' collection");
            }
            if (!collections.contains("merit_lists")) {
                database.createCollection("merit_lists");
                System.out.println("Created 'merit_lists' collection");
            }
            if (!collections.contains("hostel_status")) {
                database.createCollection("hostel_status");
                System.out.println("Created 'hostel_status' collection");
            }
            if (!collections.contains("admin")) {
                database.createCollection("admin");
                System.out.println("Created 'admin' collection");
            }

            // Ensure student admission number and roll number uniqueness
            MongoCollection<Document> studentCollection = database.getCollection("students");
            studentCollection.createIndex(Indexes.ascending("admissionNumber"), new IndexOptions().unique(true));
            studentCollection.createIndex(Indexes.ascending("rollNumber"), new IndexOptions().unique(true));

            // Ensure course code is unique
            MongoCollection<Document> courseCollection = database.getCollection("courses");
            courseCollection.createIndex(Indexes.ascending("programCode"), new IndexOptions().unique(true));

            // Ensure admin username is unique
            MongoCollection<Document> adminCollection = database.getCollection("admin");
            adminCollection.createIndex(Indexes.ascending("username"), new IndexOptions().unique(true));

            // Seed default courses, students, and applications if empty
            seedDefaultData();
        } catch (Exception e) {
            System.err.println("Error creating collections: " + e.getMessage());
        }
    }

    private static void seedDefaultData() {
        try {
            MongoCollection<Document> courseCollection = database.getCollection("courses");
            MongoCollection<Document> studentCollection = database.getCollection("students");
            MongoCollection<Document> appCollection = database.getCollection("applications");

            // 1. Seed courses if empty
            if (courseCollection.countDocuments() == 0) {
                List<Document> defaultCourses = new ArrayList<>();
                defaultCourses.add(new Document("programName", "Computer Science & Engineering")
                    .append("programCode", "CSE101")
                    .append("department", "Engineering")
                    .append("totalSeats", 120)
                    .append("availableSeats", 120)
                    .append("minCutoff", 75.0)
                    .append("description", "4-year B.Tech program in Computer Science and Engineering")
                    .append("duration", 4));

                defaultCourses.add(new Document("programName", "Electronics & Communication Engineering")
                    .append("programCode", "ECE102")
                    .append("department", "Engineering")
                    .append("totalSeats", 120)
                    .append("availableSeats", 120)
                    .append("minCutoff", 70.0)
                    .append("description", "4-year B.Tech program in Electronics and Communication Engineering")
                    .append("duration", 4));

                defaultCourses.add(new Document("programName", "Mechanical Engineering")
                    .append("programCode", "ME103")
                    .append("department", "Engineering")
                    .append("totalSeats", 60)
                    .append("availableSeats", 60)
                    .append("minCutoff", 65.0)
                    .append("description", "4-year B.Tech program in Mechanical Engineering")
                    .append("duration", 4));

                defaultCourses.add(new Document("programName", "Civil Engineering")
                    .append("programCode", "CE104")
                    .append("department", "Engineering")
                    .append("totalSeats", 60)
                    .append("availableSeats", 60)
                    .append("minCutoff", 60.0)
                    .append("description", "4-year B.Tech program in Civil Engineering")
                    .append("duration", 4));

                defaultCourses.add(new Document("programName", "Electrical Engineering")
                    .append("programCode", "EE105")
                    .append("department", "Engineering")
                    .append("totalSeats", 60)
                    .append("availableSeats", 60)
                    .append("minCutoff", 68.0)
                    .append("description", "4-year B.Tech program in Electrical Engineering")
                    .append("duration", 4));

                defaultCourses.add(new Document("programName", "Information Technology")
                    .append("programCode", "IT106")
                    .append("department", "Engineering")
                    .append("totalSeats", 120)
                    .append("availableSeats", 120)
                    .append("minCutoff", 73.0)
                    .append("description", "4-year B.Tech program in Information Technology")
                    .append("duration", 4));

                courseCollection.insertMany(defaultCourses);
                System.out.println("Default engineering courses seeded successfully!");
            }

            // 2. Seed students if empty
            if (studentCollection.countDocuments() == 0) {
                List<Document> defaultStudents = new ArrayList<>();
                
                defaultStudents.add(new Document("_id", "ADM-2026-001")
                    .append("admissionNumber", "ADM-2026-001")
                    .append("rollNumber", "ROLL-101")
                    .append("firstName", "Aarav")
                    .append("lastName", "Sharma")
                    .append("email", "aarav.sharma@example.com")
                    .append("phoneNumber", "9876543210")
                    .append("address", "12, MG Road, Bangalore")
                    .append("dateOfBirth", new Date(1053000000000L))
                    .append("gender", "Male")
                    .append("tenthMarks", 92.5)
                    .append("twelfthMarks", 94.0)
                    .append("category", "General")
                    .append("hosteler", true)
                    .append("registrationDate", new Date())
                    .append("status", "Active"));

                defaultStudents.add(new Document("_id", "ADM-2026-002")
                    .append("admissionNumber", "ADM-2026-002")
                    .append("rollNumber", "ROLL-102")
                    .append("firstName", "Diya")
                    .append("lastName", "Patel")
                    .append("email", "diya.patel@example.com")
                    .append("phoneNumber", "9876543211")
                    .append("address", "45, Sector 15, Noida")
                    .append("dateOfBirth", new Date(1058227200000L))
                    .append("gender", "Female")
                    .append("tenthMarks", 88.0)
                    .append("twelfthMarks", 89.5)
                    .append("category", "OBC")
                    .append("hosteler", false)
                    .append("registrationDate", new Date())
                    .append("status", "Active"));

                defaultStudents.add(new Document("_id", "ADM-2026-003")
                    .append("admissionNumber", "ADM-2026-003")
                    .append("rollNumber", "ROLL-103")
                    .append("firstName", "Rohan")
                    .append("lastName", "Verma")
                    .append("email", "rohan.verma@example.com")
                    .append("phoneNumber", "9876543212")
                    .append("address", "78, Park Avenue, Mumbai")
                    .append("dateOfBirth", new Date(1048291200000L))
                    .append("gender", "Male")
                    .append("tenthMarks", 76.5)
                    .append("twelfthMarks", 78.0)
                    .append("category", "SC")
                    .append("hosteler", true)
                    .append("registrationDate", new Date())
                    .append("status", "Active"));

                defaultStudents.add(new Document("_id", "ADM-2026-004")
                    .append("admissionNumber", "ADM-2026-004")
                    .append("rollNumber", "ROLL-104")
                    .append("firstName", "Ananya")
                    .append("lastName", "Nair")
                    .append("email", "ananya.nair@example.com")
                    .append("phoneNumber", "9876543213")
                    .append("address", "156, Anna Salai, Chennai")
                    .append("dateOfBirth", new Date(1068227200000L))
                    .append("gender", "Female")
                    .append("tenthMarks", 95.0)
                    .append("twelfthMarks", 97.5)
                    .append("category", "General")
                    .append("hosteler", false)
                    .append("registrationDate", new Date())
                    .append("status", "Active"));

                defaultStudents.add(new Document("_id", "ADM-2026-005")
                    .append("admissionNumber", "ADM-2026-005")
                    .append("rollNumber", "ROLL-105")
                    .append("firstName", "Kabir")
                    .append("lastName", "Singh")
                    .append("email", "kabir.singh@example.com")
                    .append("phoneNumber", "9876543214")
                    .append("address", "22, Mall Road, Shimla")
                    .append("dateOfBirth", new Date(1045227200000L))
                    .append("gender", "Male")
                    .append("tenthMarks", 68.0)
                    .append("twelfthMarks", 72.5)
                    .append("category", "ST")
                    .append("hosteler", true)
                    .append("registrationDate", new Date())
                    .append("status", "Active"));

                studentCollection.insertMany(defaultStudents);
                System.out.println("Default students seeded successfully!");
            }

            // 3. Seed applications if empty
            if (appCollection.countDocuments() == 0) {
                // Get course IDs dynamically
                List<Document> courses = new ArrayList<>();
                for (Document doc : courseCollection.find()) {
                    courses.add(doc);
                }

                if (!courses.isEmpty()) {
                    List<Document> defaultApps = new ArrayList<>();
                    
                    // App 1: Aarav for Computer Science (CSE101)
                    Document cse = findCourseByCode(courses, "CSE101");
                    if (cse != null) {
                        defaultApps.add(new Document("studentId", "ADM-2026-001")
                            .append("programId", cse.getObjectId("_id").toString())
                            .append("applicationDate", new Date())
                            .append("status", "Pending")
                            .append("merit", 94.0)
                            .append("seatType", "General")
                            .append("collegeFees", 85000.0)
                            .append("lastModified", new Date()));
                    }

                    // App 2: Diya for Electronics (ECE102)
                    Document ece = findCourseByCode(courses, "ECE102");
                    if (ece != null) {
                        defaultApps.add(new Document("studentId", "ADM-2026-002")
                            .append("programId", ece.getObjectId("_id").toString())
                            .append("applicationDate", new Date())
                            .append("status", "Pending")
                            .append("merit", 89.5)
                            .append("seatType", "OBC")
                            .append("collegeFees", 85000.0)
                            .append("lastModified", new Date()));
                    }

                    // App 3: Rohan for Mechanical (ME103)
                    Document me = findCourseByCode(courses, "ME103");
                    if (me != null) {
                        defaultApps.add(new Document("studentId", "ADM-2026-003")
                            .append("programId", me.getObjectId("_id").toString())
                            .append("applicationDate", new Date())
                            .append("status", "Pending")
                            .append("merit", 78.0)
                            .append("seatType", "SC")
                            .append("collegeFees", 45000.0)
                            .append("lastModified", new Date()));
                    }

                    // App 4: Ananya for Computer Science (CSE101)
                    if (cse != null) {
                        defaultApps.add(new Document("studentId", "ADM-2026-004")
                            .append("programId", cse.getObjectId("_id").toString())
                            .append("applicationDate", new Date())
                            .append("status", "Pending")
                            .append("merit", 97.5)
                            .append("seatType", "General")
                            .append("collegeFees", 85000.0)
                            .append("lastModified", new Date()));
                    }

                    if (!defaultApps.isEmpty()) {
                        appCollection.insertMany(defaultApps);
                        System.out.println("Default applications seeded successfully!");
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error seeding default data: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static Document findCourseByCode(List<Document> courses, String code) {
        for (Document doc : courses) {
            if (code.equals(doc.getString("programCode"))) {
                return doc;
            }
        }
        return null;
    }

    public static MongoDatabase getDatabase() {
        if (database == null) {
            initializeConnection();
        }
        return database;
    }

    public static MongoCollection<Document> getCollection(String collectionName) {
        return getDatabase().getCollection(collectionName);
    }

    public static void closeConnection() {
        if (mongoClient != null) {
            mongoClient.close();
            System.out.println("MongoDB connection closed");
        }
    }

    public static void insertDocument(String collectionName, Document document) {
        try {
            getCollection(collectionName).insertOne(document);
            System.out.println("Document inserted successfully");
        } catch (Exception e) {
            System.err.println("Error inserting document: " + e.getMessage());
        }
    }

    public static List<Document> getAllDocuments(String collectionName) {
        try {
            List<Document> documents = new ArrayList<>();
            for (Document doc : getCollection(collectionName).find()) {
                documents.add(doc);
            }
            return documents;
        } catch (Exception e) {
            System.err.println("Error fetching documents: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public static Document getDocumentById(String collectionName, String id) {
        try {
            return getCollection(collectionName).find(new Document("_id", new org.bson.types.ObjectId(id))).first();
        } catch (Exception e) {
            System.err.println("Error fetching document: " + e.getMessage());
            return null;
        }
    }

    public static void updateDocument(String collectionName, String id, Document updates) {
        try {
            getCollection(collectionName).updateOne(
                new Document("_id", new org.bson.types.ObjectId(id)),
                new Document("$set", updates)
            );
            System.out.println("Document updated successfully");
        } catch (Exception e) {
            System.err.println("Error updating document: " + e.getMessage());
        }
    }

    public static void deleteDocument(String collectionName, String id) {
        try {
            getCollection(collectionName).deleteOne(new Document("_id", new org.bson.types.ObjectId(id)));
            System.out.println("Document deleted successfully");
        } catch (Exception e) {
            System.err.println("Error deleting document: " + e.getMessage());
        }
    }

    public static boolean isConnected() {
        try {
            getDatabase().runCommand(new Document("ping", 1));
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
