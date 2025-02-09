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
                String newText = currentText.substring(0, offset) + text + currentText.substring(offset + length);
                newText = newText.toUpperCase();

                // Handle hyphen insertion
                if (newText.length() >= 2 && !newText.contains("-")) {
                    int hyphenPos = findHyphenPosition(newText);
                    if (hyphenPos > 0) {
                        newText = newText.substring(0, hyphenPos) + "-" + newText.substring(hyphenPos);
                    }
                }

                // Split into parts
                String[] parts = newText.split("-", 2);
                String beforeHyphen = parts[0];
                String afterHyphen = parts.length > 1 ? parts[1] : "";

                // Validate parts
                if (!beforeHyphen.matches("^[A-Z]{0,3}$")) {
                    return;
                }

                // After hyphen: 1-2 letters followed by 1-4 numbers
                if (parts.length > 1) {
                    String letters = afterHyphen.replaceAll("[0-9]", "");
                    String numbers = afterHyphen.replaceAll("[A-Z]", "");
                    
                    if (letters.length() > 2 || !letters.matches("^[A-Z]*$")) {
                        return;
                    }
                    
                    if (numbers.length() > 4 || !numbers.matches("^[1-9][0-9]*$")) {
                        return;
                    }
                }

                super.replace(fb, 0, fb.getDocument().getLength(), newText, attrs);
            }

            private int findHyphenPosition(String text) {
                for (int i = 1; i <= Math.min(3, text.length()); i++) {
                    if (i == text.length() || !Character.isLetter(text.charAt(i))) {
                        return i;
                    }
                }
                return -1;
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