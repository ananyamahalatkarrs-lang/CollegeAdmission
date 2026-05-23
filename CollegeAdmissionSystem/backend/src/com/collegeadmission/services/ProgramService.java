package com.collegeadmission.services;

import com.collegeadmission.database.MongoDBConnection;
import com.collegeadmission.models.Program;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.*;

public class ProgramService {
    private static final String COLLECTION_NAME = "courses";

    public static boolean addProgram(Program program) {
        try {
            MongoCollection<Document> collection = MongoDBConnection.getCollection(COLLECTION_NAME);
            
            Document doc = new Document();
            doc.append("programName", program.getProgramName())
               .append("programCode", program.getProgramCode())
               .append("department", program.getDepartment())
               .append("totalSeats", program.getTotalSeats())
               .append("availableSeats", program.getAvailableSeats())
               .append("minCutoff", program.getMinCutoff())
               .append("description", program.getDescription())
               .append("duration", program.getDuration());
            
            collection.insertOne(doc);
            System.out.println("Program added successfully");
            return true;
        } catch (Exception e) {
            System.err.println("Error adding program: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public static List<Program> getAllPrograms() {
        List<Program> programs = new ArrayList<>();
        try {
            MongoCollection<Document> collection = MongoDBConnection.getCollection(COLLECTION_NAME);
            
            for (Document doc : collection.find()) {
                Program program = documentToProgram(doc);
                programs.add(program);
            }
        } catch (Exception e) {
            System.err.println("Error fetching programs: " + e.getMessage());
        }
        return programs;
    }

    public static Program getProgramById(String id) {
        try {
            MongoCollection<Document> collection = MongoDBConnection.getCollection(COLLECTION_NAME);
            Document doc = collection.find(new Document("_id", new ObjectId(id))).first();
            if (doc != null) {
                return documentToProgram(doc);
            }
        } catch (Exception e) {
            System.err.println("Error fetching program: " + e.getMessage());
        }
        return null;
    }

    public static boolean updateProgram(String id, Program program) {
        try {
            MongoCollection<Document> collection = MongoDBConnection.getCollection(COLLECTION_NAME);
            
            Document update = new Document();
            update.append("programName", program.getProgramName())
                  .append("availableSeats", program.getAvailableSeats())
                  .append("minCutoff", program.getMinCutoff());
            
            collection.updateOne(
                new Document("_id", new ObjectId(id)),
                new Document("$set", update)
            );
            System.out.println("Program updated successfully");
            return true;
        } catch (Exception e) {
            System.err.println("Error updating program: " + e.getMessage());
            return false;
        }
    }

    private static Program documentToProgram(Document doc) {
        Program program = new Program();
        program.setId(doc.getObjectId("_id").toString());
        program.setProgramName(doc.getString("programName"));
        program.setProgramCode(doc.getString("programCode"));
        program.setDepartment(doc.getString("department"));
        program.setTotalSeats(doc.getInteger("totalSeats"));
        program.setAvailableSeats(doc.getInteger("availableSeats"));
        program.setMinCutoff(doc.getDouble("minCutoff"));
        program.setDescription(doc.getString("description"));
        program.setDuration(doc.getInteger("duration"));
        return program;
    }
}
