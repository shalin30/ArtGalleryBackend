package org.example.json;

import lombok.Data;

import java.util.List;

@Data
public class OrderRequest {
    private String userId;
    private List<OrderItemRequest> artPieceDetails;
}
