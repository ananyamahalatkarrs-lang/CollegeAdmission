package com.collegeadmission.services;

import com.collegeadmission.database.MongoDBConnection;
import com.collegeadmission.models.Application;
import com.collegeadmission.models.Program;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.FindOneAndUpdateOptions;
import com.mongodb.client.model.ReturnDocument;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.*;

public class ApplicationService {
    private static final String COLLECTION_NAME = "applications";

    public static boolean addApplication(Application application) {
        try {
            MongoCollection<Document> collection = MongoDBConnection.getCollection(COLLECTION_NAME);
            
            // Database-level redundancy check
            Document query = new Document("studentId", application.getStudentId())
                                .append("programId", application.getProgramId());
            if (collection.find(query).first() != null) {
                System.err.println("Database redundancy check failed: Duplicate application.");
                return false;
            }
            
            Document doc = new Document();
            doc.append("studentId", application.getStudentId())
               .append("programId", application.getProgramId())
               .append("applicationDate", new Date())
               .append("status", "Pending")
               .append("merit", application.getMerit())
               .append("seatType", application.getSeatType())
               .append("collegeFees", application.getCollegeFees())
               .append("lastModified", new Date());
            
            collection.insertOne(doc);
            System.out.println("Application added successfully");
            return true;
        } catch (Exception e) {
            System.err.println("Error adding application: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public static List<Application> getAllApplications() {
        List<Application> applications = new ArrayList<>();
        try {
            MongoCollection<Document> collection = MongoDBConnection.getCollection(COLLECTION_NAME);
            
            for (Document doc : collection.find()) {
                Application app = documentToApplication(doc);
                applications.add(app);
            }
        } catch (Exception e) {
            System.err.println("Error fetching applications: " + e.getMessage());
        }
        return applications;
    }

    public static List<Application> getApplicationsByStudent(String studentId) {
        List<Application> applications = new ArrayList<>();
        try {
            MongoCollection<Document> collection = MongoDBConnection.getCollection(COLLECTION_NAME);
            
            for (Document doc : collection.find(new Document("studentId", studentId))) {
                Application app = documentToApplication(doc);
                applications.add(app);
            }
        } catch (Exception e) {
            System.err.println("Error fetching applications: " + e.getMessage());
        }
        return applications;
    }

    public static boolean updateApplicationStatus(String id, String status) {
        try {
            MongoCollection<Document> collection = MongoDBConnection.getCollection(COLLECTION_NAME);
            
            Document update = new Document();
            update.append("status", status)
                  .append("lastModified", new Date());
            
            collection.updateOne(
                new Document("_id", new ObjectId(id)),
                new Document("$set", update)
            );
            
            // If admitted, update the student's status as well
            if ("Admitted".equals(status)) {
                Document appDoc = collection.find(new Document("_id", new ObjectId(id))).first();
                if (appDoc != null) {
                    String studentId = appDoc.getString("studentId");
                    MongoCollection<Document> studentCollection = MongoDBConnection.getCollection("students");
                    studentCollection.updateOne(new Document("_id", studentId), new Document("$set", new Document("status", "Admitted")));
                }
            }
            
            System.out.println("Application status updated successfully");
            
            // Trigger Audit Log
            AuditLogService.logAction(id, "Application status changed to " + status, "System Admin");
            
            return true;
        } catch (Exception e) {
            System.err.println("Error updating application: " + e.getMessage());
            return false;
        }
    }

    /**
     * Automated Stored Procedure equivalent for evaluation
     */
    public static void evaluateApplication(Application application) {
        Program program = ProgramService.getProgramById(application.getProgramId());
        if (program != null) {
            String newStatus;
            if (application.getMerit() >= program.getMinCutoff()) {
                newStatus = "Selected";
            } else if (application.getMerit() >= program.getMinCutoff() - 10.0) {
                newStatus = "Waitlisted";
            } else {
                newStatus = "Rejected";
            }
            updateApplicationStatus(application.getId(), newStatus);
        }
    }

    /**
     * Atomic ACID-compliant seat allocation (prevents overbooking)
     */
    public static boolean allocateSeat(String applicationId, String programId) {
        try {
            MongoCollection<Document> programCollection = MongoDBConnection.getCollection("courses");
            
            // Atomic check-and-decrement to prevent overbooking
            Document query = new Document("_id", new ObjectId(programId))
                                .append("availableSeats", new Document("$gt", 0));
            Document update = new Document("$inc", new Document("availableSeats", -1));
            
            FindOneAndUpdateOptions options = new FindOneAndUpdateOptions()
                    .returnDocument(ReturnDocument.AFTER);
                    
            Document updatedProgram = programCollection.findOneAndUpdate(query, update, options);
            
            if (updatedProgram != null) {
                // Seat secured, update application
                updateApplicationStatus(applicationId, "Admitted");
                AuditLogService.logAction(applicationId, "Seat Successfully Allocated in program " + programId, "System");
                return true;
            } else {
                System.out.println("Seat allocation failed: No seats available or program not found.");
                AuditLogService.logAction(applicationId, "Seat Allocation Failed (No Seats)", "System");
                return false;
            }
        } catch (Exception e) {
            System.err.println("Error allocating seat: " + e.getMessage());
            return false;
        }
    }

    public static long getTotalApplicationsCount() {
        try {
            MongoCollection<Document> collection = MongoDBConnection.getCollection(COLLECTION_NAME);
            return collection.countDocuments();
        } catch (Exception e) {
            System.err.println("Error counting applications: " + e.getMessage());
            return 0;
        }
    }

    public static long getApplicationsCountByStatus(String status) {
        try {
            MongoCollection<Document> collection = MongoDBConnection.getCollection(COLLECTION_NAME);
            return collection.countDocuments(new Document("status", status));
        } catch (Exception e) {
            System.err.println("Error counting applications by status: " + e.getMessage());
            return 0;
        }
    }

    private static Application documentToApplication(Document doc) {
        Application application = new Application();
        application.setId(doc.getObjectId("_id").toString());
        application.setStudentId(doc.getString("studentId"));
        application.setProgramId(doc.getString("programId"));
        application.setApplicationDate((Date) doc.get("applicationDate"));
        application.setStatus(doc.getString("status"));
        application.setMerit(doc.getDouble("merit"));
        application.setSeatType(doc.getString("seatType"));
        Double fees = doc.getDouble("collegeFees");
        application.setCollegeFees(fees != null ? fees : 0.0);
        application.setLastModified((Date) doc.get("lastModified"));
        return application;
    }
}
