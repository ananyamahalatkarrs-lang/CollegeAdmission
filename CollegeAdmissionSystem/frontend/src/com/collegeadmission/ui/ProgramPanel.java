package com.collegeadmission.ui;

import com.collegeadmission.models.Program;
import com.collegeadmission.services.ProgramService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ProgramPanel extends JPanel {
    private JTextField programNameField, programCodeField, departmentField, totalSeatsField, minCutoffField, durationField;
    private JTextArea descriptionArea;
    private JTable programTable;
    private DefaultTableModel tableModel;
    private JButton addButton, refreshButton;

    public ProgramPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Create input panel
        JPanel inputPanel = createInputPanel();
        
        // Create table panel
        JPanel tablePanel = createTablePanel();

        add(inputPanel, BorderLayout.NORTH);
        add(tablePanel, BorderLayout.CENTER);

        loadPrograms();
    }

    private JPanel createInputPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 4, 10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Add Course"));

        // Program Name
        panel.add(new JLabel("Program Name:"));
        programNameField = new JTextField();
        panel.add(programNameField);

        // Program Code
        panel.add(new JLabel("Program Code:"));
        programCodeField = new JTextField();
        panel.add(programCodeField);

        // Department
        panel.add(new JLabel("Department:"));
        departmentField = new JTextField();
        panel.add(departmentField);

        // Total Seats
        panel.add(new JLabel("Total Seats:"));
        totalSeatsField = new JTextField();
        panel.add(totalSeatsField);

        // Min Cutoff
        panel.add(new JLabel("Min Cutoff:"));
        minCutoffField = new JTextField();
        panel.add(minCutoffField);

        // Duration
        panel.add(new JLabel("Duration (years):"));
        durationField = new JTextField();
        panel.add(durationField);

        // Description
        panel.add(new JLabel("Description:"));
        descriptionArea = new JTextArea(2, 20);
        descriptionArea.setLineWrap(true);
        panel.add(new JScrollPane(descriptionArea));

        // Buttons Panel
        JPanel buttonPanel = new JPanel();
        addButton = new JButton("Add Course");
        addButton.addActionListener(e -> addProgram());
        
        refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(e -> loadPrograms());

        buttonPanel.add(addButton);
        buttonPanel.add(refreshButton);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(panel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        return mainPanel;
    }

    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Courses List"));

        // Create table
        tableModel = new DefaultTableModel();
        tableModel.addColumn("ID");
        tableModel.addColumn("Program Name");
        tableModel.addColumn("Code");
        tableModel.addColumn("Department");
        tableModel.addColumn("Total Seats");
        tableModel.addColumn("Available Seats");
        tableModel.addColumn("Min Cutoff");
        tableModel.addColumn("Duration");

        programTable = new JTable(tableModel);
        programTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(programTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private void addProgram() {
        try {
            String programName = programNameField.getText().trim();
            String programCode = programCodeField.getText().trim();
            String department = departmentField.getText().trim();
            int totalSeats = Integer.parseInt(totalSeatsField.getText().trim());
            double minCutoff = Double.parseDouble(minCutoffField.getText().trim());
            int duration = Integer.parseInt(durationField.getText().trim());
            String description = descriptionArea.getText().trim();

            if (programName.isEmpty() || programCode.isEmpty() || department.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill all required fields", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Program program = new Program(programName, programCode, department, totalSeats, minCutoff, description, duration);
            
            if (ProgramService.addProgram(program)) {
                JOptionPane.showMessageDialog(this, "Program added successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                clearFields();
                loadPrograms();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to add program", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter valid numbers", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadPrograms() {
        tableModel.setRowCount(0);
        ProgramService.getAllPrograms().forEach(prog -> 
            tableModel.addRow(new Object[]{
                prog.getId(),
                prog.getProgramName(),
                prog.getProgramCode(),
                prog.getDepartment(),
                prog.getTotalSeats(),
                prog.getAvailableSeats(),
                prog.getMinCutoff(),
                prog.getDuration()
            })
        );
    }

    private void clearFields() {
        programNameField.setText("");
        programCodeField.setText("");
        departmentField.setText("");
        totalSeatsField.setText("");
        minCutoffField.setText("");
        durationField.setText("");
        descriptionArea.setText("");
    }
}
