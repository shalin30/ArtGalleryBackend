package org.example.json;

import lombok.Data;

@Data
public class PurchaseItem {
    private String artTitle;
    private int quantity;
    private double price;
}
