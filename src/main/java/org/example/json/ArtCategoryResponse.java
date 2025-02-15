package org.example.json;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class ArtCategoryResponse {

    private String categoryId;
    private String name;
    private String description;
    private HttpStatus status;
    private String message;
    private String validationResponse;
}
