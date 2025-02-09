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

                // Split into parts if hyphen exists
                String[] parts = newText.split("-", -1);
                String prefix = parts[0]; // Unterscheidungszeichen (1-3 letters)
                String suffix = parts.length > 1 ? parts[1] : ""; // Erkennungsnummer

                // Validate and format each part
                if (prefix.length() > 3 || !prefix.matches("^[A-Z]*$")) {
                    return;
                }

                // Auto-insert hyphen after district code when typing letters for Erkennungsnummer
                if (!newText.contains("-") && prefix.length() > 0 && 
                    (text.matches("[A-Z]") || text.matches("[0-9]"))) {
                    newText = prefix + "-" + (parts.length > 1 ? suffix : "");
                    parts = newText.split("-", -1);
                    suffix = parts.length > 1 ? parts[1] : "";
                }

                // Validate Erkennungsnummer format (1-2 letters followed by 1-4 numbers)
                if (suffix.length() > 0) {
                    String letters = suffix.replaceAll("[0-9]", "");
                    String numbers = suffix.replaceAll("[A-Z]", "");

                    // Check letters part (1-2 letters)
                    if (letters.length() > 2 || !letters.matches("^[A-Z]*$")) {
                        return;
                    }

                    // Check numbers part (1-4 numbers, no leading zeros)
                    if (numbers.length() > 0) {
                        if (numbers.length() > 4 || !numbers.matches("^[1-9][0-9]*$")) {
                            return;
                        }
                    }
                }

                // Check total length (max 8 characters excluding hyphen)
                String withoutHyphen = newText.replace("-", "");
                if (withoutHyphen.length() > 8) {
                    return;
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