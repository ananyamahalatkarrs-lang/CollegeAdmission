package com.collegeadmission.models;

import java.io.Serializable;

public class Program implements Serializable {
    private String _id;
    private String programName;
    private String programCode;
    private String department;
    private int totalSeats;
    private int availableSeats;
    private double minCutoff;
    private String description;
    private int duration;

    public Program() {
    }

    public Program(String programName, String programCode, String department,
                   int totalSeats, double minCutoff, String description, int duration) {
        this.programName = programName;
        this.programCode = programCode;
        this.department = department;
        this.totalSeats = totalSeats;
        this.availableSeats = totalSeats;
        this.minCutoff = minCutoff;
        this.description = description;
        this.duration = duration;
    }

    // Getters and Setters
    public String getId() {
        return _id;
    }

    public void setId(String _id) {
        this._id = _id;
    }

    public String getProgramName() {
        return programName;
    }

    public void setProgramName(String programName) {
        this.programName = programName;
    }

    public String getProgramCode() {
        return programCode;
    }

    public void setProgramCode(String programCode) {
        this.programCode = programCode;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public int getTotalSeats() {
        return totalSeats;
    }

    public void setTotalSeats(int totalSeats) {
        this.totalSeats = totalSeats;
    }

    public int getAvailableSeats() {
        return availableSeats;
    }

    public void setAvailableSeats(int availableSeats) {
        this.availableSeats = availableSeats;
    }

    public double getMinCutoff() {
        return minCutoff;
    }

    public void setMinCutoff(double minCutoff) {
        this.minCutoff = minCutoff;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    @Override
    public String toString() {
        return programName + " (" + programCode + ")";
    }
}
