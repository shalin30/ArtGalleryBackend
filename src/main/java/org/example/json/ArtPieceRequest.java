package org.example.json;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class ArtPieceRequest {
    private String artId;
    private String title;
    private String description;
    private BigDecimal price;
    private String imageUrl;
    private String categoryId;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
}
