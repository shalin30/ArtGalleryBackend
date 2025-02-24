package org.example.field;

import java.util.regex.Pattern;

public class Dimensions {
    private static final String validPattern = "^\\d{2}\\s*\\*\\s*\\d{2} inches$";
    private static final String[] errorMessage = {"reqArtDimensions","errArtDimensions"};
    private static final Pattern pattern = Pattern.compile(validPattern);

    public static String isValid(String value) {
        if(value == null || value.isEmpty()){
            return errorMessage[0];
        } else {
            return pattern.matcher(value).matches() ? "" : errorMessage[1];
        }
    }
}
