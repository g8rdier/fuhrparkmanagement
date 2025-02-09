package de.fuhrpark.ui.util;

import javax.swing.text.DefaultFormatter;
import java.text.ParseException;

public class KennzeichenFormatter extends DefaultFormatter {
    
    public KennzeichenFormatter() {
        setOverwriteMode(false);
        setAllowsInvalid(true);
        setCommitsOnValidEdit(true);
    }

    @Override
    public Object stringToValue(String text) throws ParseException {
        if (text == null || text.trim().isEmpty()) {
            return null;
        }

        // Convert to uppercase and clean
        String value = text.toUpperCase().trim();
        
        // Add hyphen if missing but format is potentially valid
        if (!value.contains("-") && value.length() >= 2) {
            // Find where district code ends (1-3 letters)
            for (int i = 1; i <= Math.min(3, value.length()); i++) {
                if (i < value.length() && Character.isLetter(value.charAt(i-1)) && Character.isLetter(value.charAt(i))) {
                    continue;
                }
                value = value.substring(0, i) + "-" + value.substring(i);
                break;
            }
        }

        // Validate complete inputs
        if (value.contains("-")) {
            String[] parts = value.split("-");
            if (parts.length == 2) {
                String district = parts[0];
                String number = parts[1];
                
                // Validate district (1-3 letters)
                if (!district.matches("[A-Z]{1,3}")) {
                    throw new ParseException("Invalid district code", 0);
                }
                
                // Validate number part (1-2 letters followed by 1-4 digits)
                if (!number.matches("[A-Z]{1,2}[1-9][0-9]{0,3}")) {
                    throw new ParseException("Invalid number part", 0);
                }
            }
        }

        return value;
    }

    @Override
    public String valueToString(Object value) throws ParseException {
        if (value == null) {
            return "";
        }
        return value.toString().toUpperCase();
    }
} 