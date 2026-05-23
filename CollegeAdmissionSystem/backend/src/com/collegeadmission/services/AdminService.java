package com.collegeadmission.services;

import com.collegeadmission.database.MongoDBConnection;
import com.collegeadmission.models.Admin;
import com.mongodb.client.MongoCollection;
import org.bson.Document;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AdminService {
    private static final String COLLECTION_NAME = "admin";

    public static boolean addAdmin(Admin admin) {
        try {
            MongoCollection<Document> collection = MongoDBConnection.getCollection(COLLECTION_NAME);
            Document doc = new Document();
            doc.append("username", admin.getUsername())
               .append("password", admin.getPassword())
               .append("email", admin.getEmail())
               .append("fullName", admin.getFullName())
               .append("role", admin.getRole())
               .append("department", admin.getDepartment())
               .append("createdDate", new Date())
               .append("status", "Active")
               .append("permissions", admin.getPermissions());
            collection.insertOne(doc);
            return true;
        } catch (Exception e) {
            System.err.println("Error adding admin: " + e.getMessage());
            return false;
        }
    }

    public static List<Admin> getAllAdmins() {
        List<Admin> admins = new ArrayList<>();
        try {
            MongoCollection<Document> collection = MongoDBConnection.getCollection(COLLECTION_NAME);
            for (Document doc : collection.find()) {
                Admin admin = new Admin();
                admin.setId(doc.getObjectId("_id").toString());
                admin.setUsername(doc.getString("username"));
                admin.setEmail(doc.getString("email"));
                admin.setFullName(doc.getString("fullName"));
                admin.setRole(doc.getString("role"));
                admin.setDepartment(doc.getString("department"));
                admin.setStatus(doc.getString("status"));
                admin.setPermissions(doc.getString("permissions"));
                admins.add(admin);
            }
        } catch (Exception e) {
            System.err.println("Error fetching admins: " + e.getMessage());
        }
        return admins;
    }
}
