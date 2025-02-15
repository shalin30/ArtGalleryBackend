package org.example.service;

import org.example.entity.Order;
import org.example.json.PurchaseHistoryResponse;
import org.example.mapper.PurchaseHistoryResponseMapper;
import org.example.repository.OrderRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import static org.mockito.ArgumentMatchers.*;

@ExtendWith(MockitoExtension.class)
class PurchaseHistoryOrchestratorServiceTest {

    @Mock
    OrderRepository orderRepository;

    @Mock
    PurchaseHistoryResponseMapper mapper;

    @InjectMocks
    PurchaseHistoryOrchestratorService purchaseHistoryOrchestratorService;
    String traceId = UUID.randomUUID().toString();

    @Test
    public void testGetPurchaseHistory(){
        List<Order> orders = new ArrayList<>();
        Order order = new Order();
        order.setOrderId(1);
        orders.add(order);

        List<PurchaseHistoryResponse> responses = new ArrayList<>();
        PurchaseHistoryResponse response = new PurchaseHistoryResponse();
        response.setOrderId(1L);
        responses.add(response);
        Mockito.when(orderRepository.findOrdersByUserId(anyInt())).thenReturn(orders);
        Mockito.when(mapper.mapOrders(anyList(),anyString())).thenReturn(responses);
        List<PurchaseHistoryResponse> purchaseHistory = purchaseHistoryOrchestratorService.getPurchaseHistory("1", traceId);
        Assertions.assertEquals(1, purchaseHistory.get(0).getOrderId());
    }

    @Test
    public void testGetPurchaseHistoryWithEmptyOrders(){
        PurchaseHistoryResponse response = new PurchaseHistoryResponse();
        response.setMessage("no orders found");
        Mockito.when(orderRepository.findOrdersByUserId(anyInt())).thenReturn(new ArrayList<>());
        Mockito.when(mapper.mapEmptyPurchaseHistoryResponse(anyString())).thenReturn(response);
        List<PurchaseHistoryResponse> purchaseHistory = purchaseHistoryOrchestratorService.getPurchaseHistory("1", traceId);
        Assertions.assertEquals("no orders found", purchaseHistory.get(0).getMessage());
    }

    @Test
    public void testGetPurchaseHistoryWithNullOrders(){
        PurchaseHistoryResponse response = new PurchaseHistoryResponse();
        response.setMessage("no orders found");
        Mockito.when(orderRepository.findOrdersByUserId(anyInt())).thenReturn(null);
        Mockito.when(mapper.mapEmptyPurchaseHistoryResponse(anyString())).thenReturn(response);
        List<PurchaseHistoryResponse> purchaseHistory = purchaseHistoryOrchestratorService.getPurchaseHistory("1", traceId);
        Assertions.assertEquals("no orders found", purchaseHistory.get(0).getMessage());
    }
}