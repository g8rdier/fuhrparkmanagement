package de.fuhrpark.ui.util;

import javax.swing.text.MaskFormatter;
import java.text.ParseException;

public class KennzeichenFormatter extends MaskFormatter {
    
    public KennzeichenFormatter() {
        try {
            // U = uppercase letter, # = number
            setMask("UUU-UU####");
            setPlaceholderCharacter('_');
            setValueContainsLiteralCharacters(false);
            setAllowsInvalid(false);
            setOverwriteMode(true);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Object stringToValue(String text) throws ParseException {
        // Remove placeholder characters
        String value = text.replace("_", "").toUpperCase();
        
        // Validate format: 1-3 letters, hyphen, 1-2 letters, 1-4 numbers
        if (!value.matches("^[A-Z]{1,3}-[A-Z]{1,2}[1-9][0-9]{0,3}$")) {
            throw new ParseException("Invalid license plate format", 0);
        }
        
        return value;
    }
} 