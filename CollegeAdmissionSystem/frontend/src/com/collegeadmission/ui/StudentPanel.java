package com.collegeadmission.ui;

import com.collegeadmission.client.DataSyncManager;
import com.collegeadmission.models.Student;
import com.collegeadmission.services.StudentService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class StudentPanel extends JPanel {
    private JTextField admissionNumberField, rollNumberField;
    private JTextField firstNameField, lastNameField, emailField, phoneField, addressField;
    private JTextField tenthMarksField, twelfthMarksField;
    private JComboBox<String> genderCombo, categoryCombo;
    private JSpinner dobSpinner;
    private JRadioButton hostelerYesRadio, hostelerNoRadio;
    private ButtonGroup hostelerGroup;
    private JTable studentTable;
    private DefaultTableModel tableModel;
    private JButton addButton, updateButton, refreshButton, deleteButton;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private String selectedRoomType = "Non-AC 3 Sharing";
    private double selectedHostelCost = 60000.0;
    private String selectedStudentId = null;

    public StudentPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Create input panel
        JPanel inputPanel = createInputPanel();
        
        // Create table panel
        JPanel tablePanel = createTablePanel();

        add(inputPanel, BorderLayout.NORTH);
        add(tablePanel, BorderLayout.CENTER);

        loadStudents();
    }

    private JPanel createInputPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(7, 4, 10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Add/Edit Student"));

        // Admission Number
        panel.add(new JLabel("Admission No:"));
        admissionNumberField = new JTextField();
        panel.add(admissionNumberField);

        // Roll Number / Student ID
        panel.add(new JLabel("Student ID:"));
        rollNumberField = new JTextField();
        panel.add(rollNumberField);

        // First Name
        panel.add(new JLabel("First Name:"));
        firstNameField = new JTextField();
        panel.add(firstNameField);

        // Last Name
        panel.add(new JLabel("Last Name:"));
        lastNameField = new JTextField();
        panel.add(lastNameField);

        // Email
        panel.add(new JLabel("Email:"));
        emailField = new JTextField();
        panel.add(emailField);

        // Phone
        panel.add(new JLabel("Phone:"));
        phoneField = new JTextField();
        panel.add(phoneField);

        // Address
        panel.add(new JLabel("Address:"));
        addressField = new JTextField();
        panel.add(addressField);

        // Date of Birth
        panel.add(new JLabel("Date of Birth:"));
        dobSpinner = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(dobSpinner, "yyyy-MM-dd");
        dobSpinner.setEditor(dateEditor);
        panel.add(dobSpinner);

        // Gender
        panel.add(new JLabel("Gender:"));
        genderCombo = new JComboBox<>(new String[]{"Male", "Female", "Other"});
        panel.add(genderCombo);

        // Category
        panel.add(new JLabel("Category:"));
        categoryCombo = new JComboBox<>(new String[]{"General", "OBC", "SC", "ST"});
        panel.add(categoryCombo);

        // Hosteler
        panel.add(new JLabel("Hosteler:"));
        JPanel hostelerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        hostelerYesRadio = new JRadioButton("Yes");
        hostelerNoRadio = new JRadioButton("No", true);
        hostelerGroup = new ButtonGroup();
        hostelerGroup.add(hostelerYesRadio);
        hostelerGroup.add(hostelerNoRadio);
        
        hostelerYesRadio.addActionListener(e -> {
            if (hostelerYesRadio.isSelected()) showHostelApplicationForm();
        });
        
        hostelerPanel.add(hostelerYesRadio);
        hostelerPanel.add(hostelerNoRadio);
        panel.add(hostelerPanel);

        // 10th Marks
        panel.add(new JLabel("10th Marks:"));
        tenthMarksField = new JTextField();
        panel.add(tenthMarksField);

        // 12th Marks
        panel.add(new JLabel("12th Marks:"));
        twelfthMarksField = new JTextField();
        panel.add(twelfthMarksField);

        // Buttons Panel
        JPanel buttonPanel = new JPanel();
        addButton = new JButton("Add Student");
        addButton.addActionListener(e -> addStudent());
        
        updateButton = new JButton("Update Selected");
        updateButton.addActionListener(e -> updateStudent());
        
        refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(e -> loadStudents());
        
        deleteButton = new JButton("Delete Selected");
        deleteButton.addActionListener(e -> deleteStudent());

        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(refreshButton);
        buttonPanel.add(deleteButton);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(panel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        return mainPanel;
    }

    private void showHostelApplicationForm() {
        JDialog dialog = new JDialog(SwingUtilities.getWindowAncestor(this), "Hostel Application Form", Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setSize(350, 250);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout(10, 10));
        
        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        panel.add(new JLabel("Room Sharing Type:"));
        JComboBox<String> roomCombo = new JComboBox<>(new String[]{
            "AC Single Room", "AC 2 Sharing", "Non-AC 2 Sharing", "Non-AC 3 Sharing"
        });
        panel.add(roomCombo);
        
        panel.add(new JLabel("Estimated Cost (₹):"));
        JTextField costField = new JTextField("60000");
        costField.setEditable(false);
        panel.add(costField);
        
        roomCombo.addActionListener(e -> {
            String selected = (String) roomCombo.getSelectedItem();
            if (selected.contains("AC Single Room")) costField.setText("120000");
            else if (selected.contains("AC 2 Sharing")) costField.setText("90000");
            else if (selected.contains("Non-AC 2 Sharing")) costField.setText("75000");
            else costField.setText("60000");
        });
        
        JPanel btnPanel = new JPanel();
        JButton saveBtn = new JButton("Save & Continue");
        saveBtn.addActionListener(e -> {
            selectedRoomType = (String) roomCombo.getSelectedItem();
            selectedHostelCost = Double.parseDouble(costField.getText());
            dialog.dispose();
        });
        JButton cancelBtn = new JButton("Cancel");
        cancelBtn.addActionListener(e -> {
            hostelerNoRadio.setSelected(true);
            dialog.dispose();
        });
        btnPanel.add(saveBtn);
        btnPanel.add(cancelBtn);
        
        dialog.add(panel, BorderLayout.CENTER);
        dialog.add(btnPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Students List"));

        // Create table
        tableModel = new DefaultTableModel();
        tableModel.addColumn("Admission No");
        tableModel.addColumn("Student ID");
        tableModel.addColumn("First Name");
        tableModel.addColumn("Last Name");
        tableModel.addColumn("Email");
        tableModel.addColumn("Phone");
        tableModel.addColumn("Hosteler");
        tableModel.addColumn("10th Marks");
        tableModel.addColumn("12th Marks");
        tableModel.addColumn("Status");

        studentTable = new JTable(tableModel);
        studentTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        studentTable.setFont(new Font("Arial", Font.PLAIN, 12));
        studentTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = studentTable.getSelectedRow();
                if (selectedRow != -1) {
                    String admissionNumber = (String) tableModel.getValueAt(selectedRow, 0);
                    Student student = StudentService.getStudentById(admissionNumber);
                    if (student != null) {
                        selectedStudentId = student.getAdmissionNumber();
                        admissionNumberField.setText(student.getAdmissionNumber());
                        rollNumberField.setText(student.getRollNumber());
                        firstNameField.setText(student.getFirstName());
                        lastNameField.setText(student.getLastName());
                        emailField.setText(student.getEmail());
                        phoneField.setText(student.getPhoneNumber());
                        addressField.setText(student.getAddress() != null ? student.getAddress() : "");
                        if (student.getDateOfBirth() != null) {
                            dobSpinner.setValue(student.getDateOfBirth());
                        }
                        genderCombo.setSelectedItem(student.getGender() != null ? student.getGender() : "Male");
                        categoryCombo.setSelectedItem(student.getCategory() != null ? student.getCategory() : "General");
                        boolean hostelerValue = student.isHosteler();
                        hostelerYesRadio.setSelected(hostelerValue);
                        hostelerNoRadio.setSelected(!hostelerValue);
                        tenthMarksField.setText(String.valueOf(student.getTenthMarks()));
                        twelfthMarksField.setText(String.valueOf(student.getTwelfthMarks()));
                    }
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(studentTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private void addStudent() {
        try {
            String admissionNumber = admissionNumberField.getText().trim();
            String rollNumber = rollNumberField.getText().trim();
            String firstName = firstNameField.getText().trim();
            String lastName = lastNameField.getText().trim();
            String email = emailField.getText().trim();
            String phone = phoneField.getText().trim();
            String address = addressField.getText().trim();
            Date dob = (Date) dobSpinner.getValue();
            String gender = (String) genderCombo.getSelectedItem();
            String category = (String) categoryCombo.getSelectedItem();
            boolean hosteler = hostelerYesRadio.isSelected();
            double tenthMarks = Double.parseDouble(tenthMarksField.getText().trim());
            double twelfthMarks = Double.parseDouble(twelfthMarksField.getText().trim());

            if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || admissionNumber.isEmpty() || rollNumber.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill all required fields", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Student student = new Student(rollNumber, admissionNumber, firstName, lastName, email, phone, address, dob, gender, tenthMarks, twelfthMarks, category, hosteler);
            
            DataSyncManager.syncStudentData(student, (success, message) -> {
                if (success) {
                    if (hosteler) {
                        com.collegeadmission.services.HostelService.submitHostelApplication(admissionNumber, selectedRoomType, selectedHostelCost);
                    }
                    JOptionPane.showMessageDialog(this, message, "Success", JOptionPane.INFORMATION_MESSAGE);
                    clearFields();
                    loadStudents();
                } else {
                    JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
                }
            });

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter valid marks", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateStudent() {
        int selectedRow = studentTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a student to update", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            String admissionNumber = admissionNumberField.getText().trim();
            String rollNumber = rollNumberField.getText().trim();
            String firstName = firstNameField.getText().trim();
            String lastName = lastNameField.getText().trim();
            String email = emailField.getText().trim();
            String phone = phoneField.getText().trim();
            String address = addressField.getText().trim();
            Date dob = (Date) dobSpinner.getValue();
            String gender = (String) genderCombo.getSelectedItem();
            String category = (String) categoryCombo.getSelectedItem();
            boolean hosteler = hostelerYesRadio.isSelected();
            double tenthMarks = Double.parseDouble(tenthMarksField.getText().trim());
            double twelfthMarks = Double.parseDouble(twelfthMarksField.getText().trim());

            if (admissionNumber.isEmpty() || rollNumber.isEmpty() || firstName.isEmpty() || lastName.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill all required fields", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String studentIdToUpdate = selectedStudentId != null ? selectedStudentId : admissionNumber;
            Student student = new Student(rollNumber, admissionNumber, firstName, lastName, email, phone, address, dob, gender, tenthMarks, twelfthMarks, category, hosteler);
            if (StudentService.updateStudent(studentIdToUpdate, student)) {
                JOptionPane.showMessageDialog(this, "Student updated successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                loadStudents();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to update student", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter valid marks", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadStudents() {
        DataSyncManager.fetchAllStudents(students -> {
            SwingUtilities.invokeLater(() -> {
                tableModel.setRowCount(0);
                if (students == null || students.isEmpty()) {
                    return;
                }
                for (Student student : students) {
                    tableModel.addRow(new Object[]{
                        student.getAdmissionNumber(),
                        student.getRollNumber(),
                        student.getFirstName(),
                        student.getLastName(),
                        student.getEmail(),
                        student.getPhoneNumber(),
                        student.isHosteler() ? "Yes" : "No",
                        student.getTenthMarks(),
                        student.getTwelfthMarks(),
                        student.getStatus()
                    });
                }
            });
        });
    }

    private void deleteStudent() {
        int selectedRow = studentTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a student to delete", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String studentId = (String) tableModel.getValueAt(selectedRow, 0);
        int option = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this student?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        
        if (option == JOptionPane.YES_OPTION) {
            StudentService.deleteStudent(studentId);
            JOptionPane.showMessageDialog(this, "Student deleted successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
            loadStudents();
        }
    }

    private void clearFields() {
        admissionNumberField.setText("");
        rollNumberField.setText("");
        firstNameField.setText("");
        lastNameField.setText("");
        emailField.setText("");
        phoneField.setText("");
        addressField.setText("");
        tenthMarksField.setText("");
        twelfthMarksField.setText("");
        dobSpinner.setValue(new Date());
        genderCombo.setSelectedIndex(0);
        categoryCombo.setSelectedIndex(0);
        hostelerNoRadio.setSelected(true);
        selectedRoomType = "Non-AC 3 Sharing";
        selectedHostelCost = 60000.0;
        selectedStudentId = null;
    }

    public void refreshData() {
        loadStudents();
    }
}
