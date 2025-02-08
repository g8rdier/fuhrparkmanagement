package de.fuhrpark.ui;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import java.text.NumberFormat;
import java.util.Locale;

public class PriceDocument extends PlainDocument {
    private final NumberFormat format = NumberFormat.getNumberInstance(Locale.GERMANY);
    
    public PriceDocument() {
        format.setGroupingUsed(true);
        format.setMaximumFractionDigits(2);
        format.setMinimumFractionDigits(2);
    }

    @Override
    public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
        if (str == null) return;
        
        // Only allow digits and comma
        String newStr = str.replaceAll("[^0-9,]", "");
        if (newStr.isEmpty()) return;
        
        String currentText = getText(0, getLength());
        String beforeOffset = currentText.substring(0, offs);
        String afterOffset = currentText.substring(offs);
        String proposedResult = beforeOffset + newStr + afterOffset;
        
        // Remove all formatting
        proposedResult = proposedResult.replace(".", "").replace(",", ".");
        
        try {
            // Try to parse the number
            double value = Double.parseDouble(proposedResult);
            if (value > 999999999.99) return; // Limit to reasonable amount
            
            // Format the entire text
            String formatted = format.format(value);
            
            // Replace entire content
            super.remove(0, getLength());
            super.insertString(0, formatted, a);
        } catch (NumberFormatException e) {
            // Invalid number, ignore input
        }
    }
} 