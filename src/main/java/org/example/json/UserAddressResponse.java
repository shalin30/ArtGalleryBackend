package org.example.json;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class UserAddressResponse {
    private Address address;
    private HttpStatus status;
    private String message;
}
