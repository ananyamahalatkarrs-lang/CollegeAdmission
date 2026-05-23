package com.collegeadmission.ui;

import com.collegeadmission.models.Application;
import com.collegeadmission.models.Payment;
import com.collegeadmission.services.ApplicationService;
import com.collegeadmission.services.PaymentService;
import com.collegeadmission.services.StudentService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class PaymentPanel extends JPanel {
    private JTable paymentTable;
    private DefaultTableModel tableModel;
    private JComboBox<String> applicationCombo;
    private JTextField amountField, transactionIdField;
    private JComboBox<String> paymentMethodCombo;
    private JButton processButton, refreshButton;

    public PaymentPanel() {
        setLayout(new BorderLayout(15, 15));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        setBackground(new Color(245, 247, 250)); // Modern backdrop

        add(createInputPanel(), BorderLayout.NORTH);
        add(createTablePanel(), BorderLayout.CENTER);

        loadPayments();
        generateTransactionId(); // Auto-generate on load
    }

    private JPanel createInputPanel() {
        JPanel panel = new JPanel(new GridLayout(3, 4, 10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Process Fee Payment"));

        panel.add(new JLabel("Application ID:"));
        applicationCombo = new JComboBox<>();
        loadApplicationCombo();
        
        // Auto-populate amount based on selected application fees
        applicationCombo.addActionListener(e -> {
            String selectedApp = (String) applicationCombo.getSelectedItem();
            if (selectedApp != null && selectedApp.contains(" - ")) {
                String appId = selectedApp.split(" - ")[0];
                List<Application> apps = ApplicationService.getAllApplications();
                for (Application app : apps) {
                    if (app.getId().equals(appId)) {
                        amountField.setText(String.valueOf((int) app.getCollegeFees()));
                        break;
                    }
                }
            } else {
                amountField.setText("");
            }
        });
        
        panel.add(applicationCombo);

        panel.add(new JLabel("Amount (₹):"));
        amountField = new JTextField();
        amountField.addActionListener(e -> processPayment()); // Pressing Enter inside amount field processes the payment!
        
        // Trigger auto-population initially
        if (applicationCombo.getItemCount() > 0) {
            String selectedApp = (String) applicationCombo.getSelectedItem();
            if (selectedApp != null && selectedApp.contains(" - ")) {
                String appId = selectedApp.split(" - ")[0];
                List<Application> apps = ApplicationService.getAllApplications();
                for (Application app : apps) {
                    if (app.getId().equals(appId)) {
                        amountField.setText(String.valueOf((int) app.getCollegeFees()));
                        break;
                    }
                }
            }
        }
        
        panel.add(amountField);

        panel.add(new JLabel("Payment Method:"));
        paymentMethodCombo = new JComboBox<>(new String[]{"Credit Card", "Debit Card", "UPI", "Bank Transfer"});
        panel.add(paymentMethodCombo);

        panel.add(new JLabel("Transaction ID:"));
        transactionIdField = new JTextField();
        transactionIdField.setFont(new Font("Segoe UI", Font.BOLD, 13));
        transactionIdField.setEditable(true); // Make it editable so they can manually type in a transaction ID if they want!
        panel.add(transactionIdField);

        JPanel btnPanel = new JPanel();
        processButton = new JButton("Process Payment");
        processButton.addActionListener(e -> processPayment());
        refreshButton = new JButton("Refresh Data");
        refreshButton.addActionListener(e -> {
            loadApplicationCombo();
            loadPayments();
        });
        
        btnPanel.add(processButton);
        btnPanel.add(refreshButton);
 
        // Quick button to regenerate transaction ID manually if needed
        JButton regenBtn = new JButton("Regen Txn ID");
        regenBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        regenBtn.addActionListener(e -> generateTransactionId());
        btnPanel.add(regenBtn);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(panel, BorderLayout.CENTER);
        mainPanel.add(btnPanel, BorderLayout.SOUTH);

        return mainPanel;
    }

    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Fee Payment Records"));

        tableModel = new DefaultTableModel();
        tableModel.addColumn("Payment ID");
        tableModel.addColumn("Student ID");
        tableModel.addColumn("Application ID");
        tableModel.addColumn("Amount");
        tableModel.addColumn("Method");
        tableModel.addColumn("Transaction ID");
        tableModel.addColumn("Date");

        paymentTable = new JTable(tableModel);
        paymentTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        panel.add(new JScrollPane(paymentTable), BorderLayout.CENTER);
        return panel;
    }

    private void loadApplicationCombo() {
        applicationCombo.removeAllItems();
        List<Application> apps = ApplicationService.getAllApplications();
        for (Application app : apps) {
            if ("Documents Verified".equals(app.getStatus()) || "Pending".equals(app.getStatus())) {
                applicationCombo.addItem(app.getId() + " - Amount: ₹" + (int)app.getCollegeFees());
            }
        }
    }

    private void loadPayments() {
        tableModel.setRowCount(0);
        List<Payment> payments = PaymentService.getAllPayments();
        for (Payment p : payments) {
            tableModel.addRow(new Object[]{
                p.getId(), p.getStudentId(), p.getApplicationId(), p.getAmount(), 
                p.getPaymentMethod(), p.getTransactionId(), p.getPaymentDate()
            });
        }
    }

    private void generateTransactionId() {
        String id = "TXN-" + (System.currentTimeMillis() % 100000000L) + "-" + ((int)(Math.random() * 900) + 100);
        transactionIdField.setText(id);
    }

    private void processPayment() {
        try {
            String selectedApp = (String) applicationCombo.getSelectedItem();
            if (selectedApp == null) {
                JOptionPane.showMessageDialog(this, "Please select an application", "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            String appId = selectedApp.split(" - ")[0];
            String studentId = selectedApp.split(" - ")[1];
            
            String amountStr = amountField.getText().trim();
            if (amountStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter a payment amount!", "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            double amount;
            try {
                amount = Double.parseDouble(amountStr);
            } catch (NumberFormatException nfe) {
                JOptionPane.showMessageDialog(this, "Please enter a valid numeric amount!", "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String method = (String) paymentMethodCombo.getSelectedItem();
            String txnId = transactionIdField.getText().trim();
            
            if (txnId.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Transaction ID is missing. Generating one...", "Error", JOptionPane.ERROR_MESSAGE);
                generateTransactionId();
                return;
            }
 
            Payment payment = new Payment(studentId, appId, amount, method);
            payment.setTransactionId(txnId);
            
            if (PaymentService.addPayment(payment)) {
                // Update Application Status automatically to 'Fee Paid'
                ApplicationService.updateApplicationStatus(appId, "Fee Paid");
                JOptionPane.showMessageDialog(this, "Payment processed successfully!\nTransaction ID: " + txnId + "\nStatus updated to 'Fee Paid'.", "Success", JOptionPane.INFORMATION_MESSAGE);
                amountField.setText("");
                loadApplicationCombo();
                loadPayments();
                generateTransactionId(); // Auto-generate a new one for the next payment
            } else {
                JOptionPane.showMessageDialog(this, "Failed to process payment in database", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error processing payment: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void refreshData() {
        loadApplicationCombo();
        loadPayments();
    }
}
