package com.collegeadmission.models;

import java.io.Serializable;

public class HostelStatus implements Serializable {
    private String _id;
    private String studentId;
    private String hostelType;
    private String roomNumber;
    private String allocatedHostel;
    private String status;
    private String preferences;

    public HostelStatus() {
    }

    public HostelStatus(String studentId, String hostelType) {
        this.studentId = studentId;
        this.hostelType = hostelType;
        this.status = "Pending";
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

    public String getHostelType() {
        return hostelType;
    }

    public void setHostelType(String hostelType) {
        this.hostelType = hostelType;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public String getAllocatedHostel() {
        return allocatedHostel;
    }

    public void setAllocatedHostel(String allocatedHostel) {
        this.allocatedHostel = allocatedHostel;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPreferences() {
        return preferences;
    }

    public void setPreferences(String preferences) {
        this.preferences = preferences;
    }

    @Override
    public String toString() {
        return "HostelStatus{" +
                "studentId='" + studentId + '\'' +
                ", hostelType='" + hostelType + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
