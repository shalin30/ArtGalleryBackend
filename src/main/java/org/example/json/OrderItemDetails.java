package org.example.json;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemDetails {
    private String artPieceId;
    private String price;
    private String artTitle;
    private String quantity;
    private String imageUrl;
}
