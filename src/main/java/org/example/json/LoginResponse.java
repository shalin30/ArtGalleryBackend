package org.example.json;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class LoginResponse {
    private String token;
    private String userId;
    private String  userName;
    private HttpStatus status;
}
