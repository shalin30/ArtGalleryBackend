package org.example.json;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class Address {
    private String name;
    private String address1;
    private String address2;
    private String city;
    private String state;
    private String postalCode;
    private String phoneNumber;

}
