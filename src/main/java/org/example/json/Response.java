package org.example.json;

import lombok.Data;

@Data
public class Response {
    private int returnCode;
    private String status;
    private String message;

}
