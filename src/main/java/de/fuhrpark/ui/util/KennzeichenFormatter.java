package de.fuhrpark.ui.util;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import javax.swing.text.PlainDocument;

public class KennzeichenFormatter extends PlainDocument {
    
    public KennzeichenFormatter() {
        setDocumentFilter(new DocumentFilter() {
            @Override
            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) 
                    throws BadLocationException {
                replace(fb, offset, 0, string.toUpperCase(), attr);
            }

            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) 
                    throws BadLocationException {
                String currentText = fb.getDocument().getText(0, fb.getDocument().getLength());
                String newText = currentText.substring(0, offset) + text.toUpperCase() + currentText.substring(offset + length);

                // Split into parts
                String[] parts = newText.split("-", -1);
                String prefix = parts[0]; // First part (1-3 letters)
                String suffix = parts.length > 1 ? parts[1] : ""; // Second part (1-2 letters + 1-4 numbers)

                // Validate first part (1-3 letters)
                if (!prefix.matches("^[A-Z]{1,3}$")) {
                    return;
                }

                // Auto-insert hyphen after first part
                if (prefix.matches("^[A-Z]{1,3}$") && !newText.contains("-") && 
                    (text.matches("[A-Z]") || text.matches("[0-9]"))) {
                    newText = prefix + "-" + text.toUpperCase();
                }

                // Validate second part
                if (parts.length > 1) {
                    // Split second part into letters and numbers
                    String letters = suffix.replaceAll("[0-9]", "");
                    String numbers = suffix.replaceAll("[A-Z]", "");

                    // Check letters (1-2)
                    if (!letters.matches("^[A-Z]{0,2}$")) {
                        return;
                    }

                    // Check numbers (1-4, no leading zero)
                    if (numbers.length() > 0) {
                        if (!numbers.matches("^[1-9][0-9]{0,3}$")) {
                            return;
                        }
                    }
                }

                super.replace(fb, 0, fb.getDocument().getLength(), newText, attrs);
            }
        });
    }

    public boolean isValid() {
        try {
            String text = getText(0, getLength());
            // Must match: 1-3 letters, hyphen, 1-2 letters, 1-4 numbers
            return text.matches("^[A-Z]{1,3}-[A-Z]{1,2}[1-9][0-9]{0,3}$");
        } catch (BadLocationException e) {
            return false;
        }
    }
} 