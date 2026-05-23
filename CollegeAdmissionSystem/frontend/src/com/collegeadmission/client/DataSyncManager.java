package com.collegeadmission.client;

import com.collegeadmission.models.Student;
import com.collegeadmission.services.StudentService;
import java.util.ArrayList;
import java.util.List;

public class DataSyncManager {
    
    public static void syncStudentData(Student student, SyncListener listener) {
        new Thread(() -> {
            try {
                boolean success = StudentService.addStudent(student);
                String message = success ? "Student data synchronized" : "Failed to save student. Admission No or Student ID may already exist.";
                if (listener != null) {
                    listener.onSyncComplete(success, message);
                }
            } catch (Exception e) {
                if (listener != null) {
                    listener.onSyncComplete(false, "Error: " + e.getMessage());
                }
            }
        }).start();
    }

    public static void fetchAllStudents(FetchListener listener) {
        new Thread(() -> {
            try {
                List<Student> students = StudentService.getAllStudents();
                if (listener != null) {
                    listener.onFetchComplete(students);
                }
            } catch (Exception e) {
                System.err.println("Error fetching students: " + e.getMessage());
                if (listener != null) {
                    listener.onFetchComplete(new ArrayList<>());
                }
            }
        }).start();
    }

    public interface SyncListener {
        void onSyncComplete(boolean success, String message);
    }

    @FunctionalInterface
    public interface FetchListener {
        void onFetchComplete(List<Student> students);
    }
}
