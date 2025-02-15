package org.example.json;

import lombok.Data;
import org.springframework.http.HttpStatus;

import java.util.List;

@Data
public class PurchaseHistoryResponse {
    private Long orderId;
    private String orderDate;
    private Double totalAmount;
    private List<PurchaseItem> items;
    private HttpStatus status;
    private String message;
    private String validationResponse;
}
