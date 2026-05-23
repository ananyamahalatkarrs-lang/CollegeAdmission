package com.collegeadmission.ui;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Reusable Input Validation Utility for all panels.
 * Enforces domain-specific data types on JTextFields using DocumentFilters.
 * VARCHAR fields only accept letters/spaces, NUMBER fields only accept digits/decimals, etc.
 */
public class InputValidator {

    // =====================================================
    // DOCUMENT FILTERS (prevent invalid keystrokes entirely)
    // =====================================================

    /**
     * Only allows digits and at most one decimal point (for marks, fees, merit, amount).
     */
    public static void applyNumericFilter(JTextField field) {
        ((AbstractDocument) field.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
                if (string != null && isValidNumeric(fb.getDocument().getText(0, fb.getDocument().getLength()), string, offset)) {
                    super.insertString(fb, offset, string, attr);
                } else {
                    flashError(field);
                }
            }

            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                if (text != null && isValidNumeric(fb.getDocument().getText(0, fb.getDocument().getLength()), text, offset)) {
                    super.replace(fb, offset, length, text, attrs);
                } else {
                    flashError(field);
                }
            }

            private boolean isValidNumeric(String currentText, String newText, int offset) {
                // Build what the resulting text would be
                StringBuilder sb = new StringBuilder(currentText);
                sb.insert(offset, newText);
                String result = sb.toString();
                // Allow empty (user is clearing)
                if (result.isEmpty()) return true;
                // Allow a single minus at start
                if (result.equals("-")) return true;
                // Try parsing as double
                try {
                    Double.parseDouble(result);
                    return true;
                } catch (NumberFormatException e) {
                    // Allow trailing dot like "85."
                    if (result.endsWith(".") && result.chars().filter(c -> c == '.').count() == 1) {
                        try {
                            Double.parseDouble(result.substring(0, result.length() - 1));
                            return true;
                        } catch (NumberFormatException ex) {
                            return false;
                        }
                    }
                    return false;
                }
            }
        });
        field.setToolTipText("Only numeric values allowed (e.g. 85.5)");
    }

    /**
     * Only allows digits (for phone numbers - no decimals, no letters).
     */
    public static void applyPhoneFilter(JTextField field) {
        ((AbstractDocument) field.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
                if (string != null && string.matches("[0-9]*")) {
                    // Limit to 10 digits
                    int currentLen = fb.getDocument().getLength();
                    if (currentLen + string.length() <= 10) {
                        super.insertString(fb, offset, string, attr);
                    } else {
                        flashError(field);
                    }
                } else {
                    flashError(field);
                }
            }

            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                if (text != null && text.matches("[0-9]*")) {
                    int currentLen = fb.getDocument().getLength() - length;
                    if (currentLen + text.length() <= 10) {
                        super.replace(fb, offset, length, text, attrs);
                    } else {
                        flashError(field);
                    }
                } else {
                    flashError(field);
                }
            }
        });
        field.setToolTipText("Only digits allowed (max 10 digits)");
    }

    /**
     * Only allows letters and spaces (for names - no digits, no special chars).
     */
    public static void applyAlphaFilter(JTextField field) {
        ((AbstractDocument) field.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
                if (string != null && string.matches("[a-zA-Z ]*")) {
                    super.insertString(fb, offset, string, attr);
                } else {
                    flashError(field);
                }
            }

            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                if (text != null && text.matches("[a-zA-Z ]*")) {
                    super.replace(fb, offset, length, text, attrs);
                } else {
                    flashError(field);
                }
            }
        });
        field.setToolTipText("Only letters and spaces allowed");
    }

    /**
     * Only allows alphanumeric plus hyphens (for IDs like ADM-2026-001, ROLL-101).
     */
    public static void applyAlphaNumericFilter(JTextField field) {
        ((AbstractDocument) field.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
                if (string != null && string.matches("[a-zA-Z0-9\\-]*")) {
                    super.insertString(fb, offset, string, attr);
                } else {
                    flashError(field);
                }
            }

            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                if (text != null && text.matches("[a-zA-Z0-9\\-]*")) {
                    super.replace(fb, offset, length, text, attrs);
                } else {
                    flashError(field);
                }
            }
        });
        field.setToolTipText("Only letters, digits, and hyphens allowed");
    }

    /**
     * Only allows valid email characters (letters, digits, @, ., _, -).
     */
    public static void applyEmailFilter(JTextField field) {
        ((AbstractDocument) field.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
                if (string != null && string.matches("[a-zA-Z0-9@._\\-]*")) {
                    super.insertString(fb, offset, string, attr);
                } else {
                    flashError(field);
                }
            }

            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                if (text != null && text.matches("[a-zA-Z0-9@._\\-]*")) {
                    super.replace(fb, offset, length, text, attrs);
                } else {
                    flashError(field);
                }
            }
        });
        field.setToolTipText("Valid email characters only (letters, digits, @, .)");
    }

    // =====================================================
    // VALIDATION METHODS (for submit-time checking)
    // =====================================================

    /**
     * Validates that a field is not empty. Shows error dialog if empty.
     */
    public static boolean requireNonEmpty(JTextField field, String fieldName, Component parent) {
        if (field.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(parent,
                fieldName + " is required and cannot be empty!",
                "Validation Error - Missing Data", JOptionPane.WARNING_MESSAGE);
            field.requestFocus();
            flashError(field);
            return false;
        }
        return true;
    }

    /**
     * Validates that field contains a valid double. Shows error dialog if not.
     */
    public static boolean requireValidNumber(JTextField field, String fieldName, Component parent) {
        String text = field.getText().trim();
        if (text.isEmpty()) {
            JOptionPane.showMessageDialog(parent,
                fieldName + " is required! Please enter a numeric value.",
                "Validation Error - Missing Number", JOptionPane.WARNING_MESSAGE);
            field.requestFocus();
            flashError(field);
            return false;
        }
        try {
            Double.parseDouble(text);
            return true;
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(parent,
                fieldName + " must be a valid number!\nYou entered: \"" + text + "\"\nPlease enter only digits (e.g. 85.5)",
                "Validation Error - Invalid Number", JOptionPane.ERROR_MESSAGE);
            field.requestFocus();
            flashError(field);
            return false;
        }
    }

    /**
     * Validates that field contains a valid number within a range.
     */
    public static boolean requireNumberInRange(JTextField field, String fieldName, double min, double max, Component parent) {
        if (!requireValidNumber(field, fieldName, parent)) return false;
        double val = Double.parseDouble(field.getText().trim());
        if (val < min || val > max) {
            JOptionPane.showMessageDialog(parent,
                fieldName + " must be between " + min + " and " + max + "!\nYou entered: " + val,
                "Validation Error - Out of Range", JOptionPane.ERROR_MESSAGE);
            field.requestFocus();
            flashError(field);
            return false;
        }
        return true;
    }

    /**
     * Validates email format (basic check: must contain @ and .).
     */
    public static boolean requireValidEmail(JTextField field, Component parent) {
        String email = field.getText().trim();
        if (email.isEmpty()) {
            JOptionPane.showMessageDialog(parent,
                "Email is required!",
                "Validation Error", JOptionPane.WARNING_MESSAGE);
            field.requestFocus();
            flashError(field);
            return false;
        }
        if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
            JOptionPane.showMessageDialog(parent,
                "Invalid email format!\nYou entered: \"" + email + "\"\nExpected format: name@example.com",
                "Validation Error - Invalid Email", JOptionPane.ERROR_MESSAGE);
            field.requestFocus();
            flashError(field);
            return false;
        }
        return true;
    }

    /**
     * Validates phone number (must be exactly 10 digits).
     */
    public static boolean requireValidPhone(JTextField field, Component parent) {
        String phone = field.getText().trim();
        if (phone.isEmpty()) {
            JOptionPane.showMessageDialog(parent,
                "Phone number is required!",
                "Validation Error", JOptionPane.WARNING_MESSAGE);
            field.requestFocus();
            flashError(field);
            return false;
        }
        if (!phone.matches("^[0-9]{10}$")) {
            JOptionPane.showMessageDialog(parent,
                "Phone number must be exactly 10 digits!\nYou entered: \"" + phone + "\" (" + phone.length() + " chars)",
                "Validation Error - Invalid Phone", JOptionPane.ERROR_MESSAGE);
            field.requestFocus();
            flashError(field);
            return false;
        }
        return true;
    }

    // =====================================================
    // VISUAL FEEDBACK
    // =====================================================

    /**
     * Briefly flashes the field border red to signal invalid input.
     */
    public static void flashError(JTextField field) {
        Color originalBg = field.getBackground();
        field.setBackground(new Color(255, 220, 220));
        Timer timer = new Timer(400, e -> field.setBackground(originalBg));
        timer.setRepeats(false);
        timer.start();
        Toolkit.getDefaultToolkit().beep();
    }
}
