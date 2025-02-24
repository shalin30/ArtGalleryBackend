package org.example.json;

import lombok.Data;
import org.springframework.http.HttpStatus;

import java.util.List;

@Data
public class CartResponse {
    private Integer cartId;
    private Integer artId;
    private String artName;
    private String artCategory;
    private Double artPrice;
    private Integer quantity;
    private String imageUrl;
    private HttpStatus status;
}
