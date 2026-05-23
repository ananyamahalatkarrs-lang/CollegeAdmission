package com.collegeadmission.ui;

import com.collegeadmission.models.Application;
import com.collegeadmission.models.Program;
import com.collegeadmission.services.ApplicationService;
import com.collegeadmission.services.ProgramService;
import com.collegeadmission.services.StudentService;
import com.collegeadmission.services.PaymentService;
import com.collegeadmission.models.Student;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;

public class AnalyticsPanel extends JPanel {
    private String sessionUser;
    
    // UI elements to update dynamically
    private JLabel totalAppliedLabel;
    private JLabel totalStudentsLabel;
    private JLabel underReviewLabel;
    private JLabel admittedLabel;
    private JLabel waitlistedLabel;
    private JLabel totalRevenueLabel;
    private ModernBarChart barChart;
    
    // Pipeline badge labels
    private JLabel appliedBadge;
    private JLabel docVerificationBadge;
    private JLabel feePaymentBadge;
    private JLabel admittedBadge;

    private JTabbedPane parentTabbedPane;
    private Timer autoRefreshTimer;

    public AnalyticsPanel(String sessionUser, JTabbedPane tabbedPane) {
        this.sessionUser = sessionUser;
        this.parentTabbedPane = tabbedPane;
        setLayout(new BorderLayout(15, 15));
        setBorder(new EmptyBorder(20, 20, 20, 20));
        setBackground(new Color(245, 247, 250)); // Light, modern background

        // Navbar / Header
        add(createHeaderPanel(), BorderLayout.NORTH);

        // Center Content
        JPanel centerPanel = new JPanel(new GridLayout(2, 1, 15, 15));
        centerPanel.setOpaque(false);

        // Top row: Stats Cards
        JPanel statsRow = new JPanel(new GridLayout(2, 3, 15, 15));
        statsRow.setOpaque(false);
        
        totalAppliedLabel = createValueLabel();
        totalStudentsLabel = createValueLabel();
        underReviewLabel = createValueLabel();
        admittedLabel = createValueLabel();
        waitlistedLabel = createValueLabel();
        totalRevenueLabel = createValueLabel();
        totalRevenueLabel.setForeground(new Color(46, 204, 113)); // Bright emerald green for revenue!
        
        statsRow.add(createGlassCard("Total Applied", totalAppliedLabel));
        statsRow.add(createGlassCard("Total Students", totalStudentsLabel));
        statsRow.add(createGlassCard("Under Review", underReviewLabel));
        statsRow.add(createGlassCard("Admitted", admittedLabel));
        statsRow.add(createGlassCard("Waitlisted", waitlistedLabel));
        statsRow.add(createGlassCard("Revenue Collected", totalRevenueLabel));
        centerPanel.add(statsRow);

        // Bottom row: Search & Pipeline & Chart side-by-side!
        JPanel bottomRow = new JPanel(new GridLayout(1, 2, 15, 15));
        bottomRow.setOpaque(false);
        
        JPanel leftCol = new JPanel(new BorderLayout(10, 10));
        leftCol.setOpaque(false);
        leftCol.add(createSearchBarPanel(), BorderLayout.NORTH);
        leftCol.add(createPipelinePanel(), BorderLayout.CENTER);
        
        // Right col is the gorgeous barchart
        barChart = new ModernBarChart(new ArrayList<>());
        
        JPanel chartCard = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(255, 255, 255, 200));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g2.dispose();
            }
        };
        chartCard.setOpaque(false);
        chartCard.setBorder(new EmptyBorder(15, 15, 15, 15));
        JLabel chartTitle = new JLabel("Admission Distribution Analytics");
        chartTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        chartTitle.setForeground(new Color(40, 50, 70));
        chartCard.add(chartTitle, BorderLayout.NORTH);
        chartCard.add(barChart, BorderLayout.CENTER);

        bottomRow.add(leftCol);
        bottomRow.add(chartCard);
        centerPanel.add(bottomRow);

        add(centerPanel, BorderLayout.CENTER);
        
        // Initial data load
        refreshData();
        
        // Auto-refresh dashboard every 2 seconds
        autoRefreshTimer = new Timer(2000, e -> refreshData());
        autoRefreshTimer.start();
    }

    public void refreshData() {
        long total = ApplicationService.getTotalApplicationsCount();
        long totalStudents = StudentService.getTotalStudentsCount();
        long pending = ApplicationService.getApplicationsCountByStatus("Pending")
                     + ApplicationService.getApplicationsCountByStatus("Applied")
                     + ApplicationService.getApplicationsCountByStatus("Documents Verified")
                     + ApplicationService.getApplicationsCountByStatus("Fee Paid");
        long admitted = ApplicationService.getApplicationsCountByStatus("Admitted");
        long waitlisted = ApplicationService.getApplicationsCountByStatus("Waitlisted");
        double revenue = PaymentService.getTotalRevenue();
        
        totalAppliedLabel.setText(String.valueOf(total));
        totalStudentsLabel.setText(String.valueOf(totalStudents));
        underReviewLabel.setText(String.valueOf(pending));
        admittedLabel.setText(String.valueOf(admitted));
        waitlistedLabel.setText(String.valueOf(waitlisted));
        totalRevenueLabel.setText("₹" + String.format("%,.0f", revenue));

        // Fetch counts for individual stages in the pipeline
        long appliedCount = ApplicationService.getApplicationsCountByStatus("Pending")
                           + ApplicationService.getApplicationsCountByStatus("Applied");
        long docCount = ApplicationService.getApplicationsCountByStatus("Documents Verified");
        long feeCount = ApplicationService.getApplicationsCountByStatus("Fee Paid");
        long admittedCount = ApplicationService.getApplicationsCountByStatus("Admitted");

        if (appliedBadge != null) appliedBadge.setText("1. Applied (" + appliedCount + ")");
        if (docVerificationBadge != null) docVerificationBadge.setText("2. Documents Verified (" + docCount + ")");
        if (feePaymentBadge != null) feePaymentBadge.setText("3. Fee Paid (" + feeCount + ")");
        if (admittedBadge != null) admittedBadge.setText("4. Admitted (" + admittedCount + ")");

        // Update bar chart data dynamically
        if (barChart != null) {
            java.util.List<BarData> chartData = new ArrayList<>();
            chartData.add(new BarData("Applied", appliedCount, new Color(54, 162, 235)));
            chartData.add(new BarData("Doc Verified", docCount, new Color(255, 159, 64)));
            chartData.add(new BarData("Fee Paid", feeCount, new Color(153, 102, 255)));
            chartData.add(new BarData("Admitted", admittedCount, new Color(75, 192, 192)));
            barChart.setData(chartData);
        }
    }

    private JLabel createValueLabel() {
        JLabel label = new JLabel("0");
        label.setFont(new Font("Segoe UI", Font.BOLD, 36));
        label.setForeground(new Color(40, 120, 255));
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        return label;
    }

    private JPanel createHeaderPanel() {
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        JLabel title = new JLabel("Admission Control Panel | User: " + sessionUser);
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(new Color(40, 50, 70));
        header.add(title, BorderLayout.WEST);
        
        JButton refreshBtn = new JButton("Refresh Live Data");
        refreshBtn.addActionListener(e -> refreshData());
        refreshBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.add(refreshBtn, BorderLayout.EAST);
        
        return header;
    }

    private JPanel createGlassCard(String title, JLabel valueLabel) {
        // Custom panel for Glassmorphism
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                // Semi-transparent white
                g2.setColor(new Color(255, 255, 255, 200));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                
                // Border
                g2.setColor(new Color(200, 200, 200, 150));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);
                g2.dispose();
            }
        };
        card.setOpaque(false);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        titleLabel.setForeground(new Color(100, 110, 130));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        card.add(Box.createVerticalGlue());
        card.add(titleLabel);
        card.add(Box.createRigidArea(new Dimension(0, 10)));
        card.add(valueLabel);
        card.add(Box.createVerticalGlue());

        return card;
    }

    private JPanel createSearchBarPanel() {
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setOpaque(false);
        JLabel searchLabel = new JLabel("Quick Search (Admission No): ");
        searchLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        JTextField searchBar = new JTextField(30);
        searchBar.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        searchBar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        
        searchBar.addActionListener(e -> performQuickSearch(searchBar.getText().trim()));

        searchPanel.add(searchLabel);
        searchPanel.add(searchBar);
        return searchPanel;
    }

    private void performQuickSearch(String admissionNo) {
        if (admissionNo.isEmpty()) return;
        
        Student student = StudentService.getStudentById(admissionNo);
        if (student == null) {
            JOptionPane.showMessageDialog(this, "No student found with Admission No: " + admissionNo, "Not Found", JOptionPane.WARNING_MESSAGE);
            return;
        }

        List<Application> apps = ApplicationService.getApplicationsByStudent(admissionNo);
        StringBuilder info = new StringBuilder();
        info.append("Name: ").append(student.getFirstName()).append(" ").append(student.getLastName()).append("\n");
        info.append("Email: ").append(student.getEmail()).append("\n");
        info.append("12th Marks: ").append(student.getTwelfthMarks()).append("%\n\n");
        
        if (apps.isEmpty()) {
            info.append("No applications submitted yet.");
        } else {
            info.append("Applications:\n");
            for (Application app : apps) {
                Program prog = ProgramService.getProgramById(app.getProgramId());
                String progName = (prog != null) ? prog.getProgramName() : app.getProgramId();
                info.append("- ").append(progName)
                    .append(" | Seat: ").append(app.getSeatType() != null ? app.getSeatType() : "N/A")
                    .append(" | Fees: \u20B9").append(app.getCollegeFees())
                    .append(" | Status: ").append(app.getStatus())
                    .append(" | Merit: ").append(app.getMerit()).append("\n");
            }
        }
        
        JOptionPane.showMessageDialog(this, info.toString(), "Student Profile: " + admissionNo, JOptionPane.INFORMATION_MESSAGE);
    }

    private JPanel createPipelinePanel() {
        JPanel pipeline = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(255, 255, 255, 200));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g2.dispose();
            }
        };
        pipeline.setOpaque(false);
        pipeline.setLayout(new BorderLayout());
        pipeline.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        JLabel title = new JLabel("Live Application Pipeline & Seats");
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        pipeline.add(title, BorderLayout.NORTH);

        // Example pipeline visualization
        JPanel stages = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 20));
        stages.setOpaque(false);
        
        appliedBadge = createStageBadge("1. Applied (0)", new Color(100, 150, 255));
        docVerificationBadge = createStageBadge("2. Documents Verified (0)", new Color(255, 165, 0));
        feePaymentBadge = createStageBadge("3. Fee Paid (0)", new Color(50, 200, 100));
        admittedBadge = createStageBadge("4. Admitted (0)", new Color(40, 180, 80));
        
        makeBadgeClickable(appliedBadge, 2);
        makeBadgeClickable(docVerificationBadge, 2);
        makeBadgeClickable(feePaymentBadge, 4);
        makeBadgeClickable(admittedBadge, 1);
        
        stages.add(appliedBadge);
        stages.add(docVerificationBadge);
        stages.add(feePaymentBadge);
        stages.add(admittedBadge);
        
        pipeline.add(stages, BorderLayout.CENTER);

        return pipeline;
    }

    private void makeBadgeClickable(JLabel badge, int tabIndex) {
        badge.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        badge.setToolTipText("Click to navigate to this tab");
        badge.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (parentTabbedPane != null) {
                    parentTabbedPane.setSelectedIndex(tabIndex);
                }
            }
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                badge.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(Color.WHITE, 2, true),
                    BorderFactory.createEmptyBorder(9, 19, 9, 19)
                ));
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                badge.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(badge.getBackground(), 1, true),
                    BorderFactory.createEmptyBorder(10, 20, 10, 20)
                ));
            }
        });
    }

    private JLabel createStageBadge(String text, Color color) {
        JLabel badge = new JLabel(text);
        badge.setOpaque(true);
        badge.setBackground(color);
        badge.setForeground(Color.WHITE);
        badge.setFont(new Font("Segoe UI", Font.BOLD, 14));
        badge.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(color, 1, true),
            BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));
        return badge;
    }
}

// ----------------------------------------------------
// Custom Chart Components for Creative WOW Factor!
// ----------------------------------------------------
class ModernBarChart extends JPanel {
    private java.util.List<BarData> data;

    public ModernBarChart(java.util.List<BarData> data) {
        this.data = data;
        setOpaque(false);
    }

    public void setData(java.util.List<BarData> data) {
        this.data = data;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (data == null || data.isEmpty()) return;

        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int width = getWidth();
        int height = getHeight();
        int padding = 35;
        int chartWidth = width - 2 * padding;
        int chartHeight = height - 2 * padding - 15;

        // Find max value
        double maxVal = 0;
        for (BarData bd : data) {
            if (bd.value > maxVal) maxVal = bd.value;
        }
        if (maxVal == 0) maxVal = 1;

        int numBars = data.size();
        int gap = 15;
        int barWidth = (chartWidth - (gap * (numBars - 1))) / numBars;

        // Draw horizontal grid lines
        g2.setColor(new Color(230, 235, 240));
        for (int i = 0; i <= 4; i++) {
            int yGrid = padding + (chartHeight * i / 4);
            g2.drawLine(padding, yGrid, padding + chartWidth, yGrid);
        }

        // Draw axes
        g2.setColor(new Color(180, 190, 200));
        g2.drawLine(padding, padding + chartHeight, padding + chartWidth, padding + chartHeight);

        FontMetrics fm = g2.getFontMetrics();

        for (int i = 0; i < numBars; i++) {
            BarData bd = data.get(i);
            int barHeight = (int) ((bd.value / maxVal) * chartHeight);
            int x = padding + i * (barWidth + gap);
            int y = padding + chartHeight - barHeight;

            // Draw Bar with elegant gradient
            GradientPaint gp = new GradientPaint(x, y, bd.color, x, y + barHeight, new Color(bd.color.getRed(), bd.color.getGreen(), bd.color.getBlue(), 80));
            g2.setPaint(gp);
            g2.fillRoundRect(x, y, barWidth, barHeight, 8, 8);

            // Draw border
            g2.setColor(bd.color);
            g2.drawRoundRect(x, y, barWidth, barHeight, 8, 8);

            // Draw Value on top of bar
            g2.setColor(new Color(60, 70, 90));
            g2.setFont(new Font("Segoe UI", Font.BOLD, 12));
            String valStr = String.valueOf((int) bd.value);
            int strW = fm.stringWidth(valStr);
            g2.drawString(valStr, x + (barWidth - strW) / 2, y - 5);

            // Draw Label under the bar
            g2.setFont(new Font("Segoe UI", Font.BOLD, 11));
            g2.setColor(new Color(110, 120, 140));
            String labelStr = bd.label;
            int labelW = fm.stringWidth(labelStr);
            g2.drawString(labelStr, x + (barWidth - labelW) / 2, padding + chartHeight + 18);
        }
        g2.dispose();
    }
}

class BarData {
    String label;
    double value;
    Color color;

    public BarData(String label, double value, Color color) {
        this.label = label;
        this.value = value;
        this.color = color;
    }
}
