package org.example.json;

import lombok.Data;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;

@Data
public class OrderRequest {
    private String userId;
    private List<OrderItemRequest> artPieceDetails;
    private String address1;
    private String address2;
    private String city;
    private String state;
    private String postalCode;
    private String phoneNumber;
    private String totalAmount;
}
