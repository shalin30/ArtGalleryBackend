package org.example.service;

import org.example.entity.ArtPiece;
import org.example.entity.Order;
import org.example.entity.OrderItem;
import org.example.entity.UserDetails;
import org.example.enums.OrderStatus;
import org.example.json.*;
import org.example.mapper.OrderResponseMapper;
import org.example.repository.ArtPieceRepository;
import org.example.repository.CartRepository;
import org.example.repository.OrderRepository;
import org.example.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.*;

@ExtendWith(MockitoExtension.class)
class OrderOrchestratorServiceTest {

    @Mock
    UserRepository userRepository;

    @Mock
    ArtPieceRepository artPieceRepository;

    @Mock
    OrderRepository orderRepository;

    @Mock
    CartRepository cartRepository;

    @Mock
    OrderResponseMapper mapper;

    @Mock
    private EmailService emailService;

    @InjectMocks
    OrderOrchestratorService orderOrchestratorService;
    String traceId = UUID.randomUUID().toString();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateOrder(){
        OrderRequest orderRequest = new OrderRequest();
        List<OrderItemRequest> orderItemRequests = new ArrayList<>();
        OrderItemRequest orderItemRequest = new OrderItemRequest();
        orderRequest.setUserId("1");
        orderItemRequest.setQuantity("1");
        orderItemRequest.setArtPieceId("1");
        orderRequest.setAddress1("test address1");
        orderRequest.setAddress2("test address2");
        orderRequest.setCity("test city");
        orderRequest.setState("test state");
        orderRequest.setPostalCode("123456");
        orderRequest.setPhoneNumber("1234567890");
        orderRequest.setTotalAmount("123.56");
        orderItemRequests.add(orderItemRequest);
        orderRequest.setArtPieceDetails(orderItemRequests);

        ArtPiece artPiece = new ArtPiece();
        artPiece.setArtId(1);
        artPiece.setPrice(10.00);

        List<OrderItem> orderItems = new ArrayList<>();
        OrderItem orderItem = new OrderItem();
        orderItem.setPrice(10.00);
        orderItem.setQuantity(1);
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
        Mockito.doNothing().when(cartRepository).deleteByUserId(anyInt());
        Mockito.doNothing().when(emailService).sendEmail(eq(savedOrder.getUser().getEmail()), eq("Art Gallery Order Confirmation"), anyString(), eq(true)
        );
        OrderResponse orderResponse = orderOrchestratorService.createOrder(orderRequest, traceId);
        Assertions.assertEquals("1", orderResponse.getUserId());
    }

    @Test
    public void testCreateOrderWithNullArtPiece(){
        OrderRequest orderRequest = new OrderRequest();
        List<OrderItemRequest> orderItemRequests = new ArrayList<>();
        OrderItemRequest orderItemRequest = new OrderItemRequest();
        orderRequest.setUserId("1");
        orderRequest.setAddress1("test address1");
        orderRequest.setAddress2("test address2");
        orderRequest.setCity("test city");
        orderRequest.setState("test state");
        orderRequest.setPostalCode("123456");
        orderRequest.setPhoneNumber("1234567890");
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
        orderRequest.setAddress1("test address1");
        orderRequest.setAddress2("test address2");
        orderRequest.setCity("test city");
        orderRequest.setState("test state");
        orderRequest.setPostalCode("123456");
        orderRequest.setPhoneNumber("1234567890");
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
        orderItem.setQuantity(2);
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
        orderItem.setQuantity(2);
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
        orderItem.setQuantity(2);
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

    @Test
    public void testGetUserAddress(){
        List<Order> orders = new ArrayList<>();
        Order order = new Order();
        order.setAddress1("test address");
        orders.add(order);

        Address address = new Address();
        address.setAddress1("test address");

        UserAddressResponse response = new UserAddressResponse();
        response.setAddress(address);

        Mockito.when(orderRepository.findLatestOrderByUserId(anyInt(), any(Pageable.class))).thenReturn(orders);
        Mockito.when(mapper.mapPreviousAddressResponse(any(Order.class), anyString())).thenReturn(response);
        UserAddressResponse userAddressResponse = orderOrchestratorService.getUserAddress("1", traceId);
        Assertions.assertEquals("test address", userAddressResponse.getAddress().getAddress1());
    }

    @Test
    public void testGetUserAddressWithNullOrder(){
        Address address = new Address();
        address.setAddress1("test address");

        UserAddressResponse response = new UserAddressResponse();
        response.setMessage("No user found");

        Mockito.when(orderRepository.findLatestOrderByUserId(anyInt(), any(Pageable.class))).thenReturn(null);
        Mockito.when(mapper.mapNoPreviousOrderFoundResponse(anyString())).thenReturn(response);
        UserAddressResponse userAddressResponse = orderOrchestratorService.getUserAddress("1", traceId);
        Assertions.assertEquals("No user found", userAddressResponse.getMessage());
    }
}