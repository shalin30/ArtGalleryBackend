package org.example.json;

import lombok.Data;

@Data
public class UserCreationRequest {

    private String userName;
    private String email;
    private String password;
    private String address1;
    private String address2;
    private String city;
    private String state;
    private String postalCode;
    private String createdAt;
    private String modifiedAt;
}
