package de.fuhrpark.ui;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import java.text.NumberFormat;
import java.util.Locale;

public class PriceDocument extends PlainDocument {
    private final NumberFormat format = NumberFormat.getCurrencyInstance(Locale.GERMANY);
    
    public PriceDocument() {
        format.setGroupingUsed(true);
        format.setMaximumFractionDigits(2);
        format.setMinimumFractionDigits(2);
    }

    @Override
    public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
        if (str == null) return;
        
        // Only allow digits, comma, and dot
        String newStr = str.replaceAll("[^0-9,.]", "");
        if (newStr.isEmpty()) return;
        
        String currentText = getText(0, getLength());
        String beforeOffset = currentText.substring(0, offs);
        String afterOffset = currentText.substring(offs);
        String proposedResult = beforeOffset + newStr + afterOffset;
        
        // Remove all formatting
        String cleaned = proposedResult.replace(".", "").replace(",", ".");
        
        try {
            // Allow partial input (like single digits)
            if (cleaned.matches("^\\d*\\.?\\d{0,2}$")) {
                // If it's just a decimal point, add a leading zero
                if (cleaned.equals(".")) cleaned = "0.";
                
                double value = cleaned.isEmpty() ? 0 : Double.parseDouble(cleaned);
                if (value <= 999999999.99) {  // Reasonable limit
                    // For partial inputs, don't format yet
                    if (cleaned.endsWith(".")) {
                        super.remove(0, getLength());
                        super.insertString(0, cleaned.replace(".", ","), a);
                    } else {
                        // Format complete numbers
                        String formatted = format.format(value);
                        // Remove currency symbol for editing
                        formatted = formatted.replace(" €", "");
                        super.remove(0, getLength());
                        super.insertString(0, formatted, a);
                    }
                }
            }
        } catch (NumberFormatException e) {
            // Invalid number, ignore input
        }
    }
} 