package org.example.json;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class UserCreationResponse {
    private String userId;
    private String userName;
    private String email;
    private String address1;
    private String address2;
    private String city;
    private String state;
    private String postalCode;
    private HttpStatus status;
    private String message;
    private String validationResponse;
}
