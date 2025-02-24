package org.example.json;

import lombok.Data;

@Data
public class UserCreationRequest {

    private String userName;
    private String email;
    private String password;
    private String createdAt;
    private String modifiedAt;
}
