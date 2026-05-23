package com.collegeadmission.models;

import java.io.Serializable;
import java.util.Date;

public class MeritList implements Serializable {
    private String _id;
    private String programId;
    private String studentId;
    private int rank;
    private double meritScore;
    private String status;
    private Date publishedDate;
    private String allocationStatus;

    public MeritList() {
    }

    public MeritList(String programId, String studentId, int rank, double meritScore) {
        this.programId = programId;
        this.studentId = studentId;
        this.rank = rank;
        this.meritScore = meritScore;
        this.status = "Published";
        this.publishedDate = new Date();
        this.allocationStatus = "Not Allocated";
    }

    // Getters and Setters
    public String getId() {
        return _id;
    }

    public void setId(String _id) {
        this._id = _id;
    }

    public String getProgramId() {
        return programId;
    }

    public void setProgramId(String programId) {
        this.programId = programId;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public double getMeritScore() {
        return meritScore;
    }

    public void setMeritScore(double meritScore) {
        this.meritScore = meritScore;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(Date publishedDate) {
        this.publishedDate = publishedDate;
    }

    public String getAllocationStatus() {
        return allocationStatus;
    }

    public void setAllocationStatus(String allocationStatus) {
        this.allocationStatus = allocationStatus;
    }

    @Override
    public String toString() {
        return "MeritList{" +
                "rank=" + rank +
                ", meritScore=" + meritScore +
                ", status='" + status + '\'' +
                '}';
    }
}
