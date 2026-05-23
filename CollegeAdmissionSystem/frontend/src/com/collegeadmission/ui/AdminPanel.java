package com.collegeadmission.ui;

import com.collegeadmission.models.Admin;
import com.collegeadmission.services.AdminService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class AdminPanel extends JPanel {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JTextField emailField;
    private JTextField fullNameField;
    private JTextField roleField;
    private JTextField departmentField;
    private JTable adminTable;
    private DefaultTableModel tableModel;
    private Timer refreshTimer;

    public AdminPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel inputPanel = createInputPanel();
        JPanel tablePanel = createTablePanel();

        add(inputPanel, BorderLayout.NORTH);
        add(tablePanel, BorderLayout.CENTER);

        loadAdmins();
        
        // Auto-refresh table every 3 seconds
        refreshTimer = new Timer(3000, e -> loadAdmins());
        refreshTimer.start();
    }

    private JPanel createInputPanel() {
        JPanel panel = new JPanel(new GridLayout(4, 4, 10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Add Admin"));

        panel.add(new JLabel("Username:"));
        usernameField = new JTextField();
        panel.add(usernameField);

        panel.add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        panel.add(passwordField);

        panel.add(new JLabel("Email:"));
        emailField = new JTextField();
        panel.add(emailField);

        panel.add(new JLabel("Full Name:"));
        fullNameField = new JTextField();
        panel.add(fullNameField);

        panel.add(new JLabel("Role:"));
        roleField = new JTextField();
        panel.add(roleField);

        panel.add(new JLabel("Department:"));
        departmentField = new JTextField();
        panel.add(departmentField);

        JButton addButton = new JButton("Add Admin");
        addButton.addActionListener(e -> addAdmin());
        panel.add(addButton);

        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(e -> loadAdmins());
        panel.add(refreshButton);

        return panel;
    }

    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Admin Users"));

        tableModel = new DefaultTableModel();
        tableModel.addColumn("Username");
        tableModel.addColumn("Email");
        tableModel.addColumn("Full Name");
        tableModel.addColumn("Role");
        tableModel.addColumn("Department");
        tableModel.addColumn("Status");

        adminTable = new JTable(tableModel);
        adminTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        panel.add(new JScrollPane(adminTable), BorderLayout.CENTER);
        return panel;
    }

    private void addAdmin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();
        String email = emailField.getText().trim();
        String fullName = fullNameField.getText().trim();
        String role = roleField.getText().trim();
        String department = departmentField.getText().trim();

        if (username.isEmpty() || password.isEmpty() || email.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill required fields", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Admin admin = new Admin(username, email, fullName, role, department);
        admin.setPassword(password);
        admin.setPermissions("full");

        if (AdminService.addAdmin(admin)) {
            JOptionPane.showMessageDialog(this, "Admin added successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
            clearFields();
            loadAdmins();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to add admin", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadAdmins() {
        tableModel.setRowCount(0);
        List<Admin> admins = AdminService.getAllAdmins();
        for (Admin admin : admins) {
            tableModel.addRow(new Object[]{
                    admin.getUsername(),
                    admin.getEmail(),
                    admin.getFullName(),
                    admin.getRole(),
                    admin.getDepartment(),
                    admin.getStatus()
            });
        }
    }

    private void clearFields() {
        usernameField.setText("");
        passwordField.setText("");
        emailField.setText("");
        fullNameField.setText("");
        roleField.setText("");
        departmentField.setText("");
    }
}
