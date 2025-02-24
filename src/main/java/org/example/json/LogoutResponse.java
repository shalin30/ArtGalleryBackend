package org.example.json;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class LogoutResponse {
    private String message;
    private HttpStatus status;
}
