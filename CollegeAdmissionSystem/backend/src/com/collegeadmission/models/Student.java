package com.collegeadmission.models;

import java.io.Serializable;
import java.util.Date;

public class Student implements Serializable {
    private String _id;
    private String rollNumber;
    private String admissionNumber;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String address;
    private Date dateOfBirth;
    private String gender;
    private double tenthMarks;
    private double twelfthMarks;
    private String category;
    private boolean hosteler;
    private Date registrationDate;
    private String status;

    public Student() {
    }

    public Student(String rollNumber, String admissionNumber, String firstName, String lastName, String email,
                   String phoneNumber, String address, Date dateOfBirth, String gender,
                   double tenthMarks, double twelfthMarks, String category, boolean hosteler) {
        this.rollNumber = rollNumber;
        this.admissionNumber = admissionNumber;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.tenthMarks = tenthMarks;
        this.twelfthMarks = twelfthMarks;
        this.category = category;
        this.hosteler = hosteler;
        this.registrationDate = new Date();
        this.status = "Active";
    }

    // Getters and Setters
    public String getId() {
        return _id;
    }

    public void setId(String _id) {
        this._id = _id;
    }

    public String getRollNumber() {
        return rollNumber;
    }

    public void setRollNumber(String rollNumber) {
        this.rollNumber = rollNumber;
    }

    public String getAdmissionNumber() {
        return admissionNumber;
    }

    public void setAdmissionNumber(String admissionNumber) {
        this.admissionNumber = admissionNumber;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public double getTenthMarks() {
        return tenthMarks;
    }

    public void setTenthMarks(double tenthMarks) {
        this.tenthMarks = tenthMarks;
    }

    public double getTwelfthMarks() {
        return twelfthMarks;
    }

    public void setTwelfthMarks(double twelfthMarks) {
        this.twelfthMarks = twelfthMarks;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Date getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(Date registrationDate) {
        this.registrationDate = registrationDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isHosteler() {
        return hosteler;
    }

    public void setHosteler(boolean hosteler) {
        this.hosteler = hosteler;
    }

    @Override
    public String toString() {
        return "Student{" +
                "admissionNumber='" + admissionNumber + '\'' +
                ", rollNumber='" + rollNumber + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
