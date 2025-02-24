package org.example.json;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class ArtPieceResponse {

    private Integer artId;
    private String title;
    private String description;
    private Double price;
    private String imageUrl;
    private String artist;
    private String year;
    private String medium;
    private String dimensions;
    private String category;
    private HttpStatus status;
    private String message;
    private String validationResponse;
}
