package org.example.field;

import java.util.regex.Pattern;

public class Medium {
    private static final String validPattern = "^([a-zA-Z](\\. |[\\-'\\. ])?)+$";
    private static final String[] errorMessage = {"reqArtMedium","errArtMedium"};
    private static final Pattern pattern = Pattern.compile(validPattern);
    private static final int MAX_LENGTH = 30;

    public static String isValid(String value) {
        if(value == null || value.isEmpty()){
            return errorMessage[0];
        } else {
            return pattern.matcher(value).matches() && value.length() <= MAX_LENGTH ? "" : errorMessage[1];
        }
    }
}
