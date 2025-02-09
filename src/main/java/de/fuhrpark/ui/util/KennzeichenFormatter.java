package de.fuhrpark.ui.util;

import javax.swing.text.DefaultFormatter;
import java.text.ParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class KennzeichenFormatter extends DefaultFormatter {
    // Matches patterns like: B-AB123, MUC-XY1234, K-A1, etc.
    private static final Pattern KENNZEICHEN_PATTERN = 
        Pattern.compile("^([A-Z]{1,3})-([A-Z]{1,2})[1-9][0-9]{0,3}$");

    @Override
    public Object stringToValue(String text) throws ParseException {
        // Remove any whitespace
        String cleanText = text.trim().toUpperCase();
        
        // If there's no hyphen but the format could be valid, add it
        if (!cleanText.contains("-") && cleanText.length() >= 2) {
            int splitPoint = findSplitPoint(cleanText);
            if (splitPoint > 0) {
                cleanText = cleanText.substring(0, splitPoint) + "-" + cleanText.substring(splitPoint);
            }
        }

        // Validate the format
        Matcher matcher = KENNZEICHEN_PATTERN.matcher(cleanText);
        if (!matcher.matches()) {
            throw new ParseException("Ungültiges Kennzeichen Format", 0);
        }

        return cleanText;
    }

    private int findSplitPoint(String text) {
        // Find where the district code ends (1-3 letters)
        for (int i = 1; i <= Math.min(3, text.length()); i++) {
            if (i == text.length()) return -1;
            char nextChar = text.charAt(i);
            if (!Character.isLetter(nextChar)) return -1;
            if (i >= 1 && i <= 3 && Character.isLetter(text.charAt(i))) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public String valueToString(Object value) {
        if (value == null) return "";
        return value.toString().toUpperCase();
    }
} 