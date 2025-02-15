package org.example.json;

import lombok.Data;

@Data
public class OrderItemRequest {
    private String artPieceId;
    private String quantity;
}
