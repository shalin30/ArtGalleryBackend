package org.example.json;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class ArtPieceResponse {

    private String artId;
    private String title;
    private String description;
    private Double price;
    private String imageUrl;
    private HttpStatus status;
    private String message;
    private String validationResponse;
}
