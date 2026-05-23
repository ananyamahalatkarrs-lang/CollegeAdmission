package com.collegeadmission.models;

import java.io.Serializable;
import java.util.Date;

public class Application implements Serializable {
    private String _id;
    private String studentId;
    private String programId;
    private Date applicationDate;
    private String status;
    private double merit;
    private String seatType;
    private double collegeFees;
    private String applicationForm;
    private Date lastModified;

    public Application() {
    }

    public Application(String studentId, String programId, double merit, String seatType, double collegeFees) {
        this.studentId = studentId;
        this.programId = programId;
        this.merit = merit;
        this.seatType = seatType;
        this.collegeFees = collegeFees;
        this.applicationDate = new Date();
        this.status = "Pending";
        this.lastModified = new Date();
    }

    public Application(String studentId, String programId, double merit) {
        this.studentId = studentId;
        this.programId = programId;
        this.merit = merit;
        this.seatType = "N/A";
        this.collegeFees = 0.0;
        this.applicationDate = new Date();
        this.status = "Pending";
        this.lastModified = new Date();
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

    public String getProgramId() {
        return programId;
    }

    public void setProgramId(String programId) {
        this.programId = programId;
    }

    public Date getApplicationDate() {
        return applicationDate;
    }

    public void setApplicationDate(Date applicationDate) {
        this.applicationDate = applicationDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getMerit() {
        return merit;
    }

    public void setMerit(double merit) {
        this.merit = merit;
    }

    public String getApplicationForm() {
        return applicationForm;
    }

    public void setApplicationForm(String applicationForm) {
        this.applicationForm = applicationForm;
    }

    public Date getLastModified() {
        return lastModified;
    }

    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }

    public String getSeatType() {
        return seatType;
    }

    public void setSeatType(String seatType) {
        this.seatType = seatType;
    }

    public double getCollegeFees() {
        return collegeFees;
    }

    public void setCollegeFees(double collegeFees) {
        this.collegeFees = collegeFees;
    }

    @Override
    public String toString() {
        return "Application{" +
                "studentId='" + studentId + '\'' +
                ", programId='" + programId + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
