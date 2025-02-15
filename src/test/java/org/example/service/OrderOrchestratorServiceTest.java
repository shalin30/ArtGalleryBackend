package org.example.service;

import org.example.entity.ArtPiece;
import org.example.entity.Order;
import org.example.entity.OrderItem;
import org.example.entity.UserDetails;
import org.example.enums.OrderStatus;
import org.example.json.OrderItemRequest;
import org.example.json.OrderRequest;
import org.example.json.OrderResponse;
import org.example.mapper.OrderResponseMapper;
import org.example.repository.ArtPieceRepository;
import org.example.repository.OrderRepository;
import org.example.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;

@ExtendWith(MockitoExtension.class)
class OrderOrchestratorServiceTest {

    @Mock
    UserRepository userRepository;

    @Mock
    ArtPieceRepository artPieceRepository;

    @Mock
    OrderRepository orderRepository;

    @Mock
    OrderResponseMapper mapper;

    @InjectMocks
    OrderOrchestratorService orderOrchestratorService;
    String traceId = UUID.randomUUID().toString();

    @Test
    public void testCreateOrder(){
        OrderRequest orderRequest = new OrderRequest();
        List<OrderItemRequest> orderItemRequests = new ArrayList<>();
        OrderItemRequest orderItemRequest = new OrderItemRequest();
        orderRequest.setUserId("1");
        orderItemRequest.setQuantity("1");
        orderItemRequest.setArtPieceId("1");
        orderItemRequests.add(orderItemRequest);
        orderRequest.setArtPieceDetails(orderItemRequests);

        ArtPiece artPiece = new ArtPiece();
        artPiece.setArtId(1);
        artPiece.setPrice(10.00);

        List<OrderItem> orderItems = new ArrayList<>();
        OrderItem orderItem = new OrderItem();
        orderItem.setPrice(10.00);
        orderItem.setArtPiece(artPiece);
        orderItems.add(orderItem);

        UserDetails userDetails = new UserDetails();
        userDetails.setUserId(1);

        Order savedOrder = new Order();
        savedOrder.setOrderId(1);
        savedOrder.setOrderItems(orderItems);
        savedOrder.setStatus(OrderStatus.PENDING);
        savedOrder.setOrderDate(LocalDateTime.now());
        savedOrder.setUser(userDetails);

        Mockito.when(userRepository.findByUserId(anyInt())).thenReturn(new UserDetails());
        Mockito.when(artPieceRepository.findByArtId(anyInt())).thenReturn(artPiece);
        Mockito.when(orderRepository.save(Mockito.any(Order.class))).thenReturn(savedOrder);
        OrderResponse orderResponse = orderOrchestratorService.createOrder(orderRequest, traceId);
        Assertions.assertEquals("1", orderResponse.getUserId());
    }

    @Test
    public void testCreateOrderWithNullArtPiece(){
        OrderRequest orderRequest = new OrderRequest();
        List<OrderItemRequest> orderItemRequests = new ArrayList<>();
        OrderItemRequest orderItemRequest = new OrderItemRequest();
        orderRequest.setUserId("1");
        orderItemRequest.setQuantity("1");
        orderItemRequest.setArtPieceId("1");
        orderItemRequests.add(orderItemRequest);
        orderRequest.setArtPieceDetails(orderItemRequests);

        OrderResponse response = new OrderResponse();
        response.setMessage("no art found");

        Mockito.when(userRepository.findByUserId(anyInt())).thenReturn(new UserDetails());
        Mockito.when(artPieceRepository.findByArtId(anyInt())).thenReturn(null);
        Mockito.when(mapper.mapNoArtPieceFoundResponse(anyString())).thenReturn(response);
        OrderResponse orderResponse = orderOrchestratorService.createOrder(orderRequest, traceId);
        Assertions.assertEquals("no art found", orderResponse.getMessage());
    }

    @Test
    public void testCreateOrderWithNullUser(){
        OrderRequest orderRequest = new OrderRequest();
        List<OrderItemRequest> orderItemRequests = new ArrayList<>();
        OrderItemRequest orderItemRequest = new OrderItemRequest();
        orderRequest.setUserId("1");
        orderItemRequest.setQuantity("1");
        orderItemRequest.setArtPieceId("1");
        orderItemRequests.add(orderItemRequest);
        orderRequest.setArtPieceDetails(orderItemRequests);

        OrderResponse response = new OrderResponse();
        response.setMessage("no user found");

        Mockito.when(userRepository.findByUserId(anyInt())).thenReturn(null);
        Mockito.when(mapper.mapNoUserFoundResponse(anyString())).thenReturn(response);
        OrderResponse orderResponse = orderOrchestratorService.createOrder(orderRequest, traceId);
        Assertions.assertEquals("no user found", orderResponse.getMessage());
    }

    @Test
    public void testUpdateOrderStatus(){
        ArtPiece artPiece = new ArtPiece();
        artPiece.setArtId(1);
        artPiece.setPrice(10.00);

        List<OrderItem> orderItems = new ArrayList<>();
        OrderItem orderItem = new OrderItem();
        orderItem.setPrice(10.00);
        orderItem.setArtPiece(artPiece);
        orderItems.add(orderItem);

        UserDetails userDetails = new UserDetails();
        userDetails.setUserId(1);

        Order savedOrder = new Order();
        savedOrder.setOrderId(1);
        savedOrder.setOrderItems(orderItems);
        savedOrder.setStatus(OrderStatus.PENDING);
        savedOrder.setOrderDate(LocalDateTime.now());
        savedOrder.setUser(userDetails);

        Mockito.when(orderRepository.findByOrderId(anyInt())).thenReturn(new Order());
        Mockito.when(orderRepository.save(Mockito.any(Order.class))).thenReturn(savedOrder);
        OrderResponse response = orderOrchestratorService.updateOrderStatus("1", OrderStatus.PENDING,traceId);
        Assertions.assertEquals("1", response.getUserId());
    }

    @Test
    public void testUpdateOrderStatusWithNullOrder(){
        OrderResponse orderResponse = new OrderResponse();
        orderResponse.setMessage("no order found");

        Mockito.when(orderRepository.findByOrderId(anyInt())).thenReturn(null);
        Mockito.when(mapper.mapNoOrderFoundResponse(anyString())).thenReturn(orderResponse);
        OrderResponse response = orderOrchestratorService.updateOrderStatus("1", OrderStatus.PENDING,traceId);
        Assertions.assertEquals("no order found", response.getMessage());
    }

    @Test
    public void testGetOrderById(){
        ArtPiece artPiece = new ArtPiece();
        artPiece.setArtId(1);
        artPiece.setPrice(10.00);

        List<OrderItem> orderItems = new ArrayList<>();
        OrderItem orderItem = new OrderItem();
        orderItem.setPrice(10.00);
        orderItem.setArtPiece(artPiece);
        orderItems.add(orderItem);

        UserDetails userDetails = new UserDetails();
        userDetails.setUserId(1);

        Order savedOrder = new Order();
        savedOrder.setOrderId(1);
        savedOrder.setOrderItems(orderItems);
        savedOrder.setStatus(OrderStatus.PENDING);
        savedOrder.setOrderDate(LocalDateTime.now());
        savedOrder.setUser(userDetails);

        Mockito.when(orderRepository.findByOrderId(anyInt())).thenReturn(savedOrder);
        OrderResponse response = orderOrchestratorService.getOrderById("1", traceId);
        Assertions.assertEquals("1", response.getOrderId());
    }

    @Test
    public void testGetOrderByIdWithNUllOrder(){
        OrderResponse orderResponse = new OrderResponse();
        orderResponse.setMessage("no order found");

        Mockito.when(orderRepository.findByOrderId(anyInt())).thenReturn(null);
        Mockito.when(mapper.mapNoOrderFoundResponse(anyString())).thenReturn(orderResponse);
        OrderResponse response = orderOrchestratorService.getOrderById("1", traceId);
        Assertions.assertEquals("no order found", response.getMessage());
    }

    @Test
    public void testGetOrderByUserId(){
        ArtPiece artPiece = new ArtPiece();
        artPiece.setArtId(1);
        artPiece.setPrice(10.00);

        List<OrderItem> orderItems = new ArrayList<>();
        OrderItem orderItem = new OrderItem();
        orderItem.setPrice(10.00);
        orderItem.setArtPiece(artPiece);
        orderItems.add(orderItem);

        UserDetails userDetails = new UserDetails();
        userDetails.setUserId(1);

        List<Order> orders = new ArrayList<>();
        Order savedOrder = new Order();
        savedOrder.setOrderId(1);
        savedOrder.setOrderItems(orderItems);
        savedOrder.setStatus(OrderStatus.PENDING);
        savedOrder.setOrderDate(LocalDateTime.now());
        savedOrder.setUser(userDetails);
        orders.add(savedOrder);

        Mockito.when(userRepository.findByUserId(anyInt())).thenReturn(userDetails);
        Mockito.when(orderRepository.findOrdersByUserId(anyInt())).thenReturn(orders);
        List<OrderResponse> orderResponses = orderOrchestratorService.getOrderByUserId("1", traceId);
        Assertions.assertEquals("1", orderResponses.get(0).getOrderId());
    }

    @Test
    public void testGetOrderByUserIdWithNullOrder(){
        UserDetails userDetails = new UserDetails();
        userDetails.setUserId(1);

        Mockito.when(userRepository.findByUserId(anyInt())).thenReturn(userDetails);
        Mockito.when(orderRepository.findOrdersByUserId(anyInt())).thenReturn(null);
        List<OrderResponse> responses = orderOrchestratorService.getOrderByUserId("1", traceId);
        Assertions.assertEquals(HttpStatus.NOT_FOUND, responses.get(0).getHttpStatus());
    }

    @Test
    public void testGetOrderByUserIdWithNullUser(){
        Mockito.when(userRepository.findByUserId(anyInt())).thenReturn(null);
        List<OrderResponse> responses = orderOrchestratorService.getOrderByUserId("1", traceId);
        Assertions.assertEquals(HttpStatus.NOT_FOUND, responses.get(0).getHttpStatus());
    }
}