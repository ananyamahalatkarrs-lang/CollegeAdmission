package com.collegeadmission.ui;

import com.collegeadmission.models.Application;
import com.collegeadmission.services.ApplicationService;
import com.collegeadmission.services.StudentService;
import com.collegeadmission.services.ProgramService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ApplicationPanel extends JPanel {
    private JComboBox<String> studentCombo, programCombo, seatTypeCombo;
    private JTextField meritField, feesField;
    private JTable applicationTable;
    private DefaultTableModel tableModel;
    private JButton addButton, refreshButton, updateStatusButton;

    public ApplicationPanel() {
        setLayout(new BorderLayout(15, 15));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        setBackground(new Color(245, 247, 250)); // Match Dashboard color scheme

        // Create input panel
        JPanel inputPanel = createInputPanel();
        
        // Create table panel
        JPanel tablePanel = createTablePanel();

        add(inputPanel, BorderLayout.NORTH);
        add(tablePanel, BorderLayout.CENTER);

        loadApplications();
    }

    private JPanel createInputPanel() {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                g2.setColor(new Color(220, 224, 230));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 15, 15);
                g2.dispose();
            }
        };
        panel.setOpaque(false);
        panel.setLayout(new GridLayout(2, 6, 15, 15));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Student
        JLabel lblStudent = new JLabel("Student:");
        lblStudent.setFont(new Font("Segoe UI", Font.BOLD, 13));
        panel.add(lblStudent);
        
        studentCombo = new JComboBox<>();
        studentCombo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        panel.add(studentCombo);

        // Program
        JLabel lblProgram = new JLabel("Program:");
        lblProgram.setFont(new Font("Segoe UI", Font.BOLD, 13));
        panel.add(lblProgram);
        
        programCombo = new JComboBox<>();
        programCombo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        panel.add(programCombo);

        // Merit
        JLabel lblMerit = new JLabel("Merit Score:");
        lblMerit.setFont(new Font("Segoe UI", Font.BOLD, 13));
        panel.add(lblMerit);
        
        meritField = new JTextField();
        meritField.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        panel.add(meritField);

        // Seat Type Selection
        JLabel lblSeat = new JLabel("Seat Type:");
        lblSeat.setFont(new Font("Segoe UI", Font.BOLD, 13));
        panel.add(lblSeat);

        String[] seats = {"Government Seat (CET)", "COMED-K", "Management Seat"};
        seatTypeCombo = new JComboBox<>(seats);
        seatTypeCombo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        panel.add(seatTypeCombo);

        // Separate College Fees
        JLabel lblFees = new JLabel("College Fees:");
        lblFees.setFont(new Font("Segoe UI", Font.BOLD, 13));
        panel.add(lblFees);

        feesField = new JTextField("170000"); // default for Government Seat (CET)
        feesField.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        feesField.setEditable(false);
        panel.add(feesField);

        // Auto-update fee text field when seatType is selected
        seatTypeCombo.addActionListener(e -> {
            String selected = (String) seatTypeCombo.getSelectedItem();
            if ("Government Seat (CET)".equals(selected)) {
                feesField.setText("170000");
            } else if ("COMED-K".equals(selected)) {
                feesField.setText("220000");
            } else if ("Management Seat".equals(selected)) {
                feesField.setText("4000000");
            }
        });

        // Placeholders to balance Grid (needs 12 components)
        panel.add(new JLabel(""));
        panel.add(new JLabel(""));

        // Load data
        loadStudentCombo();
        loadProgramCombo();

        // Buttons Panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        
        addButton = createStyledButton("Apply Now", new Color(40, 120, 255));
        addButton.addActionListener(e -> addApplication());
        
        refreshButton = createStyledButton("Refresh", new Color(110, 120, 135));
        refreshButton.addActionListener(e -> loadApplications());
        
        updateStatusButton = createStyledButton("Update Status", new Color(40, 180, 80));
        updateStatusButton.addActionListener(e -> updateStatus());

        buttonPanel.add(addButton);
        buttonPanel.add(refreshButton);
        buttonPanel.add(updateStatusButton);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setOpaque(false);
        mainPanel.add(panel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        return mainPanel;
    }

    private JButton createStyledButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setForeground(Color.WHITE);
        btn.setBackground(bg);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(bg.darker(), 1, true),
            BorderFactory.createEmptyBorder(8, 18, 8, 18)
        ));
        return btn;
    }

    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                g2.setColor(new Color(220, 224, 230));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 15, 15);
                g2.dispose();
            }
        };
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Header Title
        JLabel tableTitle = new JLabel("Applications List");
        tableTitle.setFont(new Font("Segoe UI", Font.BOLD, 15));
        tableTitle.setForeground(new Color(50, 60, 80));
        tableTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        panel.add(tableTitle, BorderLayout.NORTH);

        // Create table
        tableModel = new DefaultTableModel();
        tableModel.addColumn("ID");
        tableModel.addColumn("Student ID");
        tableModel.addColumn("Program ID");
        tableModel.addColumn("Status");
        tableModel.addColumn("Merit");
        tableModel.addColumn("Application Date");
        tableModel.addColumn("Seat Type");
        tableModel.addColumn("College Fees");

        applicationTable = new JTable(tableModel);
        applicationTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        applicationTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        applicationTable.setRowHeight(25);
        applicationTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));

        JScrollPane scrollPane = new JScrollPane(applicationTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private void loadStudentCombo() {
        studentCombo.removeAllItems();
        StudentService.getAllStudents().forEach(s -> 
            studentCombo.addItem(s.getFirstName() + " " + s.getLastName() + " (" + s.getId() + ")")
        );
    }

    private void loadProgramCombo() {
        programCombo.removeAllItems();
        ProgramService.getAllPrograms().forEach(p -> 
            programCombo.addItem(p.getProgramName() + " (" + p.getId() + ")")
        );
    }

    private void addApplication() {
        try {
            String selectedStudent = (String) studentCombo.getSelectedItem();
            String selectedProgram = (String) programCombo.getSelectedItem();

            if (selectedStudent == null || selectedStudent.trim().isEmpty() || 
                selectedProgram == null || selectedProgram.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please select both a Student and a Course/Program first!", "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String meritStr = meritField.getText().trim();
            if (meritStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter a Merit Score!", "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            double merit;
            try {
                merit = Double.parseDouble(meritStr);
            } catch (NumberFormatException nfe) {
                JOptionPane.showMessageDialog(this, "Please enter a valid numeric Merit Score!", "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String studentId = selectedStudent.substring(selectedStudent.lastIndexOf("(") + 1, selectedStudent.lastIndexOf(")"));
            String programId = selectedProgram.substring(selectedProgram.lastIndexOf("(") + 1, selectedProgram.lastIndexOf(")"));

            // Enforce duplicate application/redundancy check and popup error
            java.util.List<Application> existing = ApplicationService.getApplicationsByStudent(studentId);
            for (Application app : existing) {
                if (app.getProgramId().equals(programId)) {
                    JOptionPane.showMessageDialog(this, 
                        "Redundancy Error: Student has already applied for this course!\nDuplicate applications are not allowed.", 
                        "Duplicate Application", 
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            String seatType = (String) seatTypeCombo.getSelectedItem();
            double collegeFees = Double.parseDouble(feesField.getText().trim());

            Application application = new Application(studentId, programId, merit, seatType, collegeFees);
            
            if (ApplicationService.addApplication(application)) {
                JOptionPane.showMessageDialog(this, "Application submitted successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                meritField.setText("");
                loadApplications();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to submit application", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadApplications() {
        tableModel.setRowCount(0);
        ApplicationService.getAllApplications().forEach(app -> 
            tableModel.addRow(new Object[]{
                app.getId(),
                app.getStudentId(),
                app.getProgramId(),
                app.getStatus(),
                app.getMerit(),
                app.getApplicationDate(),
                app.getSeatType() != null ? app.getSeatType() : "N/A",
                app.getCollegeFees()
            })
        );
    }

    private void updateStatus() {
        int selectedRow = applicationTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an application", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String appId = (String) tableModel.getValueAt(selectedRow, 0);
        String[] statuses = {"Applied", "Documents Verified", "Fee Paid", "Admitted", "Waitlisted", "Rejected"};
        String newStatus = (String) JOptionPane.showInputDialog(this, "Select new status:", "Update Status", JOptionPane.QUESTION_MESSAGE, null, statuses, statuses[0]);
        
        if (newStatus != null && ApplicationService.updateApplicationStatus(appId, newStatus)) {
            JOptionPane.showMessageDialog(this, "Status updated successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
            loadApplications();
        }
    }

    public void refreshData() {
        loadStudentCombo();
        loadProgramCombo();
        loadApplications();
    }
}
