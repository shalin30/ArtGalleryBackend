package org.example.field;

import java.util.regex.Pattern;

public class Year {
    private static final String validPattern = "^[0-9]{4}$";
    private static final String[] errorMessage = {"reqArtYear","errArtYear"};
    private static final Pattern pattern = Pattern.compile(validPattern);
    private static final int MAX_LENGTH = 4;

    public static String isValid(String value) {
        if(value == null || value.isEmpty()){
            return errorMessage[0];
        } else {
            return pattern.matcher(value).matches() && value.length() <= MAX_LENGTH ? "" : errorMessage[1];
        }
    }
}
