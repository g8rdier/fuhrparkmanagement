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
                replace(fb, offset, 0, string, attr);
            }

            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) 
                    throws BadLocationException {
                String currentText = fb.getDocument().getText(0, fb.getDocument().getLength());
                String newText = currentText.substring(0, offset) + text.toUpperCase() + currentText.substring(offset + length);

                // Handle hyphen insertion
                if (!newText.contains("-")) {
                    String[] parts = newText.split("(?<=\\D)(?=\\d)|(?<=\\D)(?=\\D{3})");
                    if (parts.length > 1) {
                        newText = parts[0] + "-" + parts[1];
                    }
                }

                // Validate format
                if (newText.length() > 8) { // Max 8 characters total
                    return;
                }

                String[] parts = newText.split("-", -1);
                String prefix = parts[0]; // District code
                String suffix = parts.length > 1 ? parts[1] : ""; // Letters + numbers

                // Validate district code (1-3 letters)
                if (!prefix.matches("^[A-Z]{0,3}$")) {
                    return;
                }

                // Validate suffix (1-2 letters followed by 1-4 numbers)
                if (parts.length > 1) {
                    String letters = suffix.replaceAll("[0-9]", "");
                    String numbers = suffix.replaceAll("[A-Z]", "");

                    if (letters.length() > 2 || !letters.matches("^[A-Z]*$")) {
                        return;
                    }

                    if (numbers.length() > 4 || (numbers.length() > 0 && !numbers.matches("^[1-9][0-9]*$"))) {
                        return;
                    }
                }

                super.replace(fb, 0, fb.getDocument().getLength(), newText, attrs);
            }
        });
    }

    public boolean isValid() {
        try {
            String text = getText(0, getLength());
            return text.matches("^[A-Z]{1,3}-[A-Z]{1,2}[1-9][0-9]{0,3}$");
        } catch (BadLocationException e) {
            return false;
        }
    }
} 