package org.example.mapper;

import lombok.extern.slf4j.Slf4j;
import org.example.entity.Order;
import org.example.entity.OrderItem;
import org.example.json.PurchaseHistoryResponse;
import org.example.json.PurchaseItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class PurchaseHistoryResponseMapper {

    private final Logger logger = LoggerFactory.getLogger(PurchaseHistoryResponseMapper.class);

    public List<PurchaseHistoryResponse> mapOrders(List<Order> orders, String traceId){
        logger.info("PurchaseHistoryMapper started, traceId: {}", traceId);
        List<PurchaseHistoryResponse> response = new ArrayList<>();
        List<PurchaseItem> orderItems = new ArrayList<>();
        PurchaseItem item;
        PurchaseHistoryResponse response1;
        for(Order order : orders){
            response1 = new PurchaseHistoryResponse();
            response1.setOrderId(Long.valueOf(order.getOrderId()));
            response1.setOrderDate(String.valueOf(order.getOrderDate()));
            response1.setTotalAmount(order.getTotalAmount());
            if(order.getOrderItems() != null && !order.getOrderItems().isEmpty()) {
                for (OrderItem orderItem : order.getOrderItems()){
                    item = new PurchaseItem();
                    item.setArtTitle(orderItem.getArtPiece().getTitle());
                    item.setQuantity(orderItem.getQuantity());
                    item.setPrice(orderItem.getPrice());
                    orderItems.add(item);
                }
            }
            response1.setItems(orderItems);
            response.add(response1);
        }
        return response;
    }

    public PurchaseHistoryResponse mapEmptyPurchaseHistoryResponse(String traceId) {
        logger.info("map empty purchase history response started, traceId: {}", traceId);
        PurchaseHistoryResponse response = new PurchaseHistoryResponse();
        response.setStatus(HttpStatus.NOT_FOUND);
        response.setMessage("No purchases have been made yet");
        logger.info("map empty purchase history response ended, traceId: {}", traceId);
        return response;
    }
}
