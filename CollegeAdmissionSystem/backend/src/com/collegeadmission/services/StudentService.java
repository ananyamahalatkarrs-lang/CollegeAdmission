package com.collegeadmission.services;

import com.collegeadmission.database.MongoDBConnection;
import com.collegeadmission.models.Student;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.text.SimpleDateFormat;
import java.util.*;

public class StudentService {
    private static final String COLLECTION_NAME = "students";
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public static boolean addStudent(Student student) {
        try {
            MongoCollection<Document> collection = MongoDBConnection.getCollection(COLLECTION_NAME);
            
            String admissionNumber = student.getAdmissionNumber();
            if (admissionNumber == null || admissionNumber.trim().isEmpty()) {
                throw new IllegalArgumentException("Admission number is required");
            }
            String rollNumber = student.getRollNumber();
            if (rollNumber == null || rollNumber.trim().isEmpty()) {
                throw new IllegalArgumentException("Student ID / roll number is required");
            }

            Document duplicateQuery = new Document("$or", Arrays.asList(
                    new Document("_id", admissionNumber),
                    new Document("rollNumber", rollNumber)
            ));
            if (collection.find(duplicateQuery).first() != null) {
                System.err.println("Student data duplicate check failed: admissionNumber or rollNumber already exists.");
                return false;
            }

            Document doc = new Document();
            doc.append("_id", admissionNumber)
               .append("admissionNumber", admissionNumber)
               .append("rollNumber", rollNumber)
               .append("firstName", student.getFirstName())
               .append("lastName", student.getLastName())
               .append("email", student.getEmail())
               .append("phoneNumber", student.getPhoneNumber())
               .append("address", student.getAddress())
               .append("dateOfBirth", student.getDateOfBirth())
               .append("gender", student.getGender())
               .append("tenthMarks", student.getTenthMarks())
               .append("twelfthMarks", student.getTwelfthMarks())
               .append("category", student.getCategory())
               .append("hosteler", student.isHosteler())
               .append("registrationDate", new Date())
               .append("status", "Active");
            
            collection.insertOne(doc);
            System.out.println("Student added successfully");
            return true;
        } catch (Exception e) {
            System.err.println("Error adding student: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public static List<Student> getAllStudents() {
        List<Student> students = new ArrayList<>();
        try {
            MongoCollection<Document> collection = MongoDBConnection.getCollection(COLLECTION_NAME);
            
            for (Document doc : collection.find()) {
                Student student = documentToStudent(doc);
                students.add(student);
            }
        } catch (Exception e) {
            System.err.println("Error fetching students: " + e.getMessage());
        }
        return students;
    }

    public static Student getStudentById(String id) {
        try {
            MongoCollection<Document> collection = MongoDBConnection.getCollection(COLLECTION_NAME);
            Document doc = collection.find(new Document("_id", id)).first();
            if (doc != null) {
                return documentToStudent(doc);
            }
        } catch (Exception e) {
            System.err.println("Error fetching student: " + e.getMessage());
        }
        return null;
    }

    public static boolean updateStudent(String id, Student student) {
        try {
            MongoCollection<Document> collection = MongoDBConnection.getCollection(COLLECTION_NAME);
            
            Document update = new Document();
            update.append("rollNumber", student.getRollNumber())
                  .append("firstName", student.getFirstName())
                  .append("lastName", student.getLastName())
                  .append("email", student.getEmail())
                  .append("phoneNumber", student.getPhoneNumber())
                  .append("address", student.getAddress())
                  .append("dateOfBirth", student.getDateOfBirth())
                  .append("gender", student.getGender())
                  .append("tenthMarks", student.getTenthMarks())
                  .append("twelfthMarks", student.getTwelfthMarks())
                  .append("category", student.getCategory())
                  .append("hosteler", student.isHosteler());
            
            collection.updateOne(
                new Document("_id", id),
                new Document("$set", update)
            );
            System.out.println("Student updated successfully");
            return true;
        } catch (Exception e) {
            System.err.println("Error updating student: " + e.getMessage());
            return false;
        }
    }

    public static boolean deleteStudent(String id) {
        try {
            MongoCollection<Document> collection = MongoDBConnection.getCollection(COLLECTION_NAME);
            collection.deleteOne(new Document("_id", id));
            System.out.println("Student deleted successfully");
            return true;
        } catch (Exception e) {
            System.err.println("Error deleting student: " + e.getMessage());
            return false;
        }
    }

    public static long getTotalStudentsCount() {
        try {
            MongoCollection<Document> collection = MongoDBConnection.getCollection(COLLECTION_NAME);
            return collection.countDocuments();
        } catch (Exception e) {
            System.err.println("Error counting students: " + e.getMessage());
            return 0;
        }
    }

    public static Student getStudentByEmail(String email) {
        try {
            MongoCollection<Document> collection = MongoDBConnection.getCollection(COLLECTION_NAME);
            Document doc = collection.find(new Document("email", email)).first();
            if (doc != null) {
                return documentToStudent(doc);
            }
        } catch (Exception e) {
            System.err.println("Error fetching student by email: " + e.getMessage());
        }
        return null;
    }

    private static Student documentToStudent(Document doc) {
        Student student = new Student();
        Object idValue = doc.get("_id");
        student.setId(idValue != null ? idValue.toString() : null);
        student.setRollNumber(doc.getString("rollNumber"));
        student.setAdmissionNumber(doc.getString("admissionNumber"));
        student.setFirstName(doc.getString("firstName"));
        student.setLastName(doc.getString("lastName"));
        student.setEmail(doc.getString("email"));
        student.setPhoneNumber(doc.getString("phoneNumber"));
        student.setAddress(doc.getString("address"));
        student.setDateOfBirth((Date) doc.get("dateOfBirth"));
        student.setGender(doc.getString("gender"));

        Object tenthObj = doc.get("tenthMarks");
        student.setTenthMarks(tenthObj instanceof Number ? ((Number) tenthObj).doubleValue() : 0.0);

        Object twelfthObj = doc.get("twelfthMarks");
        student.setTwelfthMarks(twelfthObj instanceof Number ? ((Number) twelfthObj).doubleValue() : 0.0);

        student.setCategory(doc.getString("category"));
        student.setHosteler(doc.getBoolean("hosteler", false));
        student.setStatus(doc.getString("status") != null ? doc.getString("status") : "Active");
        student.setRegistrationDate((Date) doc.get("registrationDate"));
        return student;
    }
}
