package com.collegeadmission.models;

import java.io.Serializable;
import java.util.Date;

public class Payment implements Serializable {
    private String _id;
    private String studentId;
    private String applicationId;
    private double amount;
    private Date paymentDate;
    private String paymentMethod;
    private String transactionId;
    private String status;
    private String remarks;

    public Payment() {
    }

    public Payment(String studentId, String applicationId, double amount, String paymentMethod) {
        this.studentId = studentId;
        this.applicationId = applicationId;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.paymentDate = new Date();
        this.status = "Completed";
    }

    // Getters and Setters
    public String getId() {
        return _id;
    }

    public void setId(String _id) {
        this._id = _id;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Date getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    @Override
    public String toString() {
        return "Payment{" +
                "amount=" + amount +
                ", status='" + status + '\'' +
                '}';
    }
}
