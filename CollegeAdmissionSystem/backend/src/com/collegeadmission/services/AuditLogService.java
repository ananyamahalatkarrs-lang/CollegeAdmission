package com.collegeadmission.services;

import com.collegeadmission.database.MongoDBConnection;
import com.mongodb.client.MongoCollection;
import org.bson.Document;

import java.util.Date;

public class AuditLogService {
    private static final String COLLECTION_NAME = "admission_audit_log";

    public static void logAction(String entityId, String action, String performedBy) {
        try {
            MongoCollection<Document> collection = MongoDBConnection.getCollection(COLLECTION_NAME);
            
            Document logEntry = new Document()
                    .append("entityId", entityId)
                    .append("action", action)
                    .append("performedBy", performedBy)
                    .append("timestamp", new Date());
            
            collection.insertOne(logEntry);
            System.out.println("Audit Log: " + action + " on " + entityId);
        } catch (Exception e) {
            System.err.println("Error creating audit log: " + e.getMessage());
        }
    }
}
