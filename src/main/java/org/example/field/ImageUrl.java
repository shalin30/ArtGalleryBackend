package org.example.field;

import java.util.regex.Pattern;

public class ImageUrl {
    private static final String validPattern = "^(https?://.*\\.(?:jpg|jpeg|png|gif|bmp|webp))$";
    private static final String[] errorMessage = {"reqImageUrl","errImageUrl"};
    private static final Pattern pattern = Pattern.compile(validPattern);

    public static String isValid(String value) {
        if(value == null || value.isEmpty()){
            return errorMessage[0];
        } else {
            return pattern.matcher(value).matches() ? "" : errorMessage[1];
        }
    }
}
