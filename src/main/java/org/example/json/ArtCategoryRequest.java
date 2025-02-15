package org.example.json;

import lombok.Data;

import javax.persistence.Column;
import java.time.LocalDateTime;

@Data
public class ArtCategoryRequest {

    private String categoryId;
    private String name;
    private String description;
}
