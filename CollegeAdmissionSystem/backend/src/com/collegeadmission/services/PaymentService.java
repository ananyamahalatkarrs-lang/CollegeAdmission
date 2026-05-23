package com.collegeadmission.services;

import com.collegeadmission.database.MongoDBConnection;
import com.collegeadmission.models.Payment;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.*;

public class PaymentService {
    private static final String COLLECTION_NAME = "payments";

    public static boolean addPayment(Payment payment) {
        try {
            MongoCollection<Document> collection = MongoDBConnection.getCollection(COLLECTION_NAME);
            
            Document doc = new Document();
            doc.append("studentId", payment.getStudentId())
               .append("applicationId", payment.getApplicationId())
               .append("amount", payment.getAmount())
               .append("paymentDate", new Date())
               .append("paymentMethod", payment.getPaymentMethod())
               .append("transactionId", payment.getTransactionId())
               .append("status", "Completed");
            
            collection.insertOne(doc);
            System.out.println("Payment recorded successfully");
            return true;
        } catch (Exception e) {
            System.err.println("Error recording payment: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public static List<Payment> getPaymentsByStudent(String studentId) {
        List<Payment> payments = new ArrayList<>();
        try {
            MongoCollection<Document> collection = MongoDBConnection.getCollection(COLLECTION_NAME);
            
            for (Document doc : collection.find(new Document("studentId", studentId))) {
                Payment payment = documentToPayment(doc);
                payments.add(payment);
            }
        } catch (Exception e) {
            System.err.println("Error fetching payments: " + e.getMessage());
        }
        return payments;
    }

    public static List<Payment> getAllPayments() {
        List<Payment> payments = new ArrayList<>();
        try {
            MongoCollection<Document> collection = MongoDBConnection.getCollection(COLLECTION_NAME);
            
            for (Document doc : collection.find()) {
                Payment payment = documentToPayment(doc);
                payments.add(payment);
            }
        } catch (Exception e) {
            System.err.println("Error fetching payments: " + e.getMessage());
        }
        return payments;
    }

    public static double getTotalRevenue() {
        double total = 0.0;
        try {
            MongoCollection<Document> collection = MongoDBConnection.getCollection(COLLECTION_NAME);
            for (Document doc : collection.find()) {
                Double amt = doc.getDouble("amount");
                if (amt != null) {
                    total += amt;
                }
            }
        } catch (Exception e) {
            System.err.println("Error calculating total revenue: " + e.getMessage());
        }
        return total;
    }

    private static Payment documentToPayment(Document doc) {
        Payment payment = new Payment();
        payment.setId(doc.getObjectId("_id").toString());
        payment.setStudentId(doc.getString("studentId"));
        payment.setApplicationId(doc.getString("applicationId"));
        payment.setAmount(doc.getDouble("amount"));
        payment.setPaymentDate((Date) doc.get("paymentDate"));
        payment.setPaymentMethod(doc.getString("paymentMethod"));
        payment.setTransactionId(doc.getString("transactionId"));
        payment.setStatus(doc.getString("status"));
        return payment;
    }
}
