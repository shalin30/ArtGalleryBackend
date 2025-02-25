package org.example.field;

import java.util.regex.Pattern;

public class Address {
    private static final String validPattern = "^[A-Za-z\\-\\d#.\\\\/',: ]+$";
    private static final String[] errorMessage = {"reqAddress","errAddress"};
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
