package com.collegeadmission.ui;

import com.collegeadmission.client.DataSyncManager;
import com.collegeadmission.database.MongoDBConnection;
import com.collegeadmission.models.*;
import com.collegeadmission.services.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainFrame extends JFrame {
    private JTabbedPane tabbedPane;
    private StudentPanel studentPanel;
    private ApplicationPanel applicationPanel;
    private ProgramPanel programPanel;
    private PaymentPanel paymentPanel;
    private AnalyticsPanel analyticsPanel;
    private JLabel statusLabel;
    private String sessionUser;

    public MainFrame() {
        // Simulate Login Session
        sessionUser = JOptionPane.showInputDialog(null, "Enter Admin Username for Session:", "Login", JOptionPane.QUESTION_MESSAGE);
        if (sessionUser == null || sessionUser.trim().isEmpty()) {
            sessionUser = "Guest Admin";
        }

        setTitle("College Admission System - Session: " + sessionUser);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setResizable(true);

        // Initialize MongoDB connection
        MongoDBConnection.initializeConnection();

        // Create main panel
        JPanel mainPanel = new JPanel(new BorderLayout());

        // Create menu bar
        createMenuBar();

        // Create status bar
        statusLabel = new JLabel("Status: Connected to MongoDB");
        statusLabel.setBorder(BorderFactory.createEtchedBorder());
        statusLabel.setFont(new Font("Arial", Font.PLAIN, 12));

        // Create tabbed pane
        tabbedPane = new JTabbedPane();
        
        studentPanel = new StudentPanel();
        applicationPanel = new ApplicationPanel();
        programPanel = new ProgramPanel();
        paymentPanel = new PaymentPanel();
        analyticsPanel = new AnalyticsPanel(sessionUser, tabbedPane);

        tabbedPane.addTab("Live Analytics Dashboard", analyticsPanel);
        tabbedPane.addTab("Students", studentPanel);
        tabbedPane.addTab("Applications", applicationPanel);
        tabbedPane.addTab("Courses", programPanel);
        tabbedPane.addTab("Fee Payments", paymentPanel);

        // Auto-refresh panel data when switching tabs
        tabbedPane.addChangeListener(e -> {
            int idx = tabbedPane.getSelectedIndex();
            if (idx == 0) analyticsPanel.refreshData();
            else if (idx == 1) studentPanel.refreshData();
            else if (idx == 2) applicationPanel.refreshData();
            else if (idx == 4) paymentPanel.refreshData();
        });

        mainPanel.add(tabbedPane, BorderLayout.CENTER);
        mainPanel.add(statusLabel, BorderLayout.SOUTH);

        setContentPane(mainPanel);
        setVisible(true);
    }

    private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        
        // File Menu
        JMenu fileMenu = new JMenu("File");
        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(e -> {
            MongoDBConnection.closeConnection();
            System.exit(0);
        });
        fileMenu.add(exitItem);
        
        // Help Menu
        JMenu helpMenu = new JMenu("Help");
        JMenuItem aboutItem = new JMenuItem("About");
        aboutItem.addActionListener(e -> {
            JOptionPane.showMessageDialog(this,
                "College Admission System v1.0\nPowered by MongoDB and Java Swing",
                "About",
                JOptionPane.INFORMATION_MESSAGE);
        });
        helpMenu.add(aboutItem);
        
        menuBar.add(fileMenu);
        menuBar.add(helpMenu);
        
        setJMenuBar(menuBar);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainFrame());
    }
}
