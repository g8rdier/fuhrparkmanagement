package de.fuhrpark.ui.model;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import java.text.NumberFormat;
import java.util.Locale;

public class PreisDocument extends PlainDocument {
    private final NumberFormat format = NumberFormat.getCurrencyInstance(Locale.GERMANY);
    
    public PreisDocument() {
        format.setGroupingUsed(true);
        format.setMaximumFractionDigits(2);
        format.setMinimumFractionDigits(2);
    }

    @Override
    public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
        // Only allow digits, comma, dot and euro symbol
        if (str == null) return;
        
        String newStr = str.replaceAll("[^0-9,.]", "");
        String currentText = getText(0, getLength());
        String resultingText = currentText.substring(0, offs) + newStr + currentText.substring(offs);
        
        // Convert to a standard format for parsing
        String normalizedText = resultingText.replace(',', '.');
        
        try {
            // Try to parse the number to validate it
            if (!normalizedText.isEmpty()) {
                double value = Double.parseDouble(normalizedText);
                // Limit to reasonable amount (e.g., 10 million euros)
                if (value <= 10000000) {
                    super.insertString(offs, newStr, a);
                }
            } else {
                super.insertString(offs, newStr, a);
            }
        } catch (NumberFormatException e) {
            // If it's not a valid number, only allow if it's a partial number (like "1." or "1,")
            if (resultingText.matches("^\\d*[,.]?\\d*$")) {
                super.insertString(offs, newStr, a);
            }
        }
    }
} 