package com.collegeadmission.services;

import com.collegeadmission.database.MongoDBConnection;
import com.mongodb.client.MongoCollection;
import org.bson.Document;

import java.util.Date;

public class HostelService {
    private static final String COLLECTION_NAME = "hostel_status";

    public static boolean submitHostelApplication(String admissionNumber, String roomType, double cost) {
        try {
            MongoCollection<Document> collection = MongoDBConnection.getCollection(COLLECTION_NAME);
            
            Document doc = new Document();
            doc.append("admissionNumber", admissionNumber)
               .append("roomType", roomType)
               .append("cost", cost)
               .append("applicationDate", new Date())
               .append("status", "Pending");
            
            collection.insertOne(doc);
            System.out.println("Hostel application added successfully");
            return true;
        } catch (Exception e) {
            System.err.println("Error adding hostel application: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}
