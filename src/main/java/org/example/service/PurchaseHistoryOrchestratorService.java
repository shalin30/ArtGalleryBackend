package org.example.service;

import lombok.extern.slf4j.Slf4j;
import org.example.entity.Order;
import org.example.json.PurchaseHistoryResponse;
import org.example.mapper.PurchaseHistoryResponseMapper;
import org.example.repository.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class PurchaseHistoryOrchestratorService {

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    PurchaseHistoryResponseMapper mapper;

    public PurchaseHistoryOrchestratorService(OrderRepository orderRepository, PurchaseHistoryResponseMapper mapper) {
        this.orderRepository = orderRepository;
        this.mapper = mapper;
    }

    public List<PurchaseHistoryResponse> getPurchaseHistory(String userId, String traceId) {
        log.info("PurchaseHistoryOrchestratorService started, traceId: {}", traceId);
        List<PurchaseHistoryResponse> response = new ArrayList<>();
        PurchaseHistoryResponse response1;

        List<Order> orders = orderRepository.findOrdersByUserId(Integer.valueOf(userId));
        if(orders != null && !orders.isEmpty()) {
            response = mapper.mapOrders(orders,traceId);
        } else {
            response1 = mapper.mapEmptyPurchaseHistoryResponse(traceId);
            response.add(response1);
        }
        log.info("PurchaseHistoryOrchestratorService ended, traceId: {}", traceId);
        return response;
    }
}
