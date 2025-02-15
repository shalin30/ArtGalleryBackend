package org.example.json;

import lombok.Data;
import java.util.Date;

@Data
public class UserData {
    private Long userId;
    private String userName;
    private String email;
    private String password;
    private String address1;
    private String address2;
    private String city;
    private String state;
    private String postalCode;
    private Date createdAt;
    private Date modifiedAt;
}
