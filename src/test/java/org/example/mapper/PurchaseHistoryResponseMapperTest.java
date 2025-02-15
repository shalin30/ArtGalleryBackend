package org.example.mapper;

import org.example.entity.ArtPiece;
import org.example.entity.Order;
import org.example.entity.OrderItem;
import org.example.json.PurchaseHistoryResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class PurchaseHistoryResponseMapperTest {

    @InjectMocks
    PurchaseHistoryResponseMapper mapper;

    List<Order> orders;
    Order order;
    List<OrderItem> orderItems;
    OrderItem orderItem;
    ArtPiece artPiece;
    String traceId = UUID.randomUUID().toString();

    @Test
    public void testMapOrders(){
        orders = new ArrayList<>();
        order = new Order();
        orderItems = new ArrayList<>();
        orderItem = new OrderItem();
        artPiece = new ArtPiece();
        order.setOrderId(1);
        order.setOrderDate(LocalDateTime.now());
        order.setTotalAmount(10.00);
        artPiece.setTitle("test title");
        orderItem.setArtPiece(artPiece);
        orderItem.setQuantity(1);
        orderItem.setPrice(10.00);
        orderItems.add(orderItem);
        order.setOrderItems(orderItems);
        orders.add(order);

        List<PurchaseHistoryResponse> purchaseHistoryResponseList = mapper.mapOrders(orders, traceId);
        Assertions.assertEquals(1, purchaseHistoryResponseList.get(0).getOrderId());
    }

    @Test
    public void testMapEmptyPurchaseHistoryResponse(){
        PurchaseHistoryResponse response = mapper.mapEmptyPurchaseHistoryResponse(traceId);
        Assertions.assertEquals("No purchases have been made yet", response.getMessage());
    }
}