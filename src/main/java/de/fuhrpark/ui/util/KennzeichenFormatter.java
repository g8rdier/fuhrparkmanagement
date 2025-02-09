package de.fuhrpark.ui.util;

import javax.swing.text.MaskFormatter;
import java.text.ParseException;

public class KennzeichenFormatter extends MaskFormatter {
    
    public KennzeichenFormatter() {
        try {
            setMask("***-**####");  // More flexible mask
            setPlaceholder("_");     // Clear placeholder
            setValidCharacters("ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"); // Valid chars
            setValueContainsLiteralCharacters(true);  // Keep the hyphen
            setCommitsOnValidEdit(true);  // Commit on valid edits
            setAllowsInvalid(true);      // Allow temporary invalid states
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Object stringToValue(String text) throws ParseException {
        if (text == null || text.trim().isEmpty()) {
            return null;
        }

        // Clean the input
        String value = text.toUpperCase().replace("_", "").trim();
        
        // If it's a partial input, allow it
        if (value.matches("^[A-Z]{1,3}(-[A-Z]{0,2}[0-9]{0,4})?$")) {
            return value;
        }
        
        // For complete inputs, enforce strict format
        if (!value.matches("^[A-Z]{1,3}-[A-Z]{1,2}[1-9][0-9]{0,3}$")) {
            throw new ParseException("Ungültiges Kennzeichen Format", 0);
        }
        
        return value;
    }

    @Override
    public String valueToString(Object value) throws ParseException {
        if (value == null) {
            return "";
        }
        return super.valueToString(value);
    }
} 