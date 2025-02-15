package org.example.json;

import lombok.Data;
import org.springframework.http.HttpStatus;

import java.util.List;

@Data
public class OrderResponse {
    private String orderId;
    private String userId;
    private String orderDate;
    private String status;
    private String totalAmount;
    private List<OrderItemDetails> orderItems;
    private HttpStatus httpStatus;
    private String message;
    private String validationResponse;
}
