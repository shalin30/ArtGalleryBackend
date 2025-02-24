package org.example.controller;

import org.example.enums.OrderStatus;
import org.example.json.*;
import org.example.service.OrderOrchestratorService;
import org.example.service.PasswordResetService;
import org.example.service.PurchaseHistoryOrchestratorService;
import org.example.service.UserOrchestratorService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.*;
import static org.mockito.ArgumentMatchers.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    UserOrchestratorService userOrchestratorService;

    @Mock
    PurchaseHistoryOrchestratorService purchaseHistoryOrchestratorService;

    @Mock
    OrderOrchestratorService orderOrchestratorService;

    @Mock
    private PasswordResetService resetService;

    @InjectMocks
    UserController controller;

    UserCreationResponse userCreationResponse;
    List<PurchaseHistoryResponse> purchaseHistoryResponseList;
    PurchaseHistoryResponse purchaseHistoryResponse;
    List<OrderResponse> orderResponseList;
    OrderResponse orderResponse;

    String traceId = UUID.randomUUID().toString();

    @Test
    public void testCreateUser(){
        userCreationResponse= new UserCreationResponse();
        userCreationResponse.setStatus(HttpStatus.OK);
        Mockito.when(userOrchestratorService.createUser(any(UserCreationRequest.class), anyString())).thenReturn(userCreationResponse);
        ResponseEntity<UserCreationResponse> response = controller.createUser(new UserCreationRequest(), traceId);
        Assertions.assertEquals(HttpStatus.OK, Objects.requireNonNull(response.getBody()).getStatus());
    }

    @Test
    public void testPurchaseHistory(){
        purchaseHistoryResponseList = new ArrayList<>();
        purchaseHistoryResponse = new PurchaseHistoryResponse();
        purchaseHistoryResponse.setStatus(HttpStatus.OK);
        purchaseHistoryResponseList.add(purchaseHistoryResponse);
        Mockito.when(purchaseHistoryOrchestratorService.getPurchaseHistory(anyString(), anyString())).thenReturn(purchaseHistoryResponseList);
        ResponseEntity<List<PurchaseHistoryResponse>> response = controller.purchaseHistory("1", traceId);
        Assertions.assertEquals(HttpStatus.OK, Objects.requireNonNull(response.getBody()).get(0).getStatus());
    }

    @Test
    public void testPurchaseHistoryWithNotFoundResponse(){
        purchaseHistoryResponseList = new ArrayList<>();
        purchaseHistoryResponse = new PurchaseHistoryResponse();
        purchaseHistoryResponse.setStatus(HttpStatus.NOT_FOUND);
        purchaseHistoryResponseList.add(purchaseHistoryResponse);
        Mockito.when(purchaseHistoryOrchestratorService.getPurchaseHistory(anyString(), anyString())).thenReturn(purchaseHistoryResponseList);
        ResponseEntity<List<PurchaseHistoryResponse>> response = controller.purchaseHistory("1", traceId);
        Assertions.assertEquals(HttpStatus.NOT_FOUND, Objects.requireNonNull(response.getBody()).get(0).getStatus());
    }

    @Test
    public void testUpdateUser(){
        userCreationResponse= new UserCreationResponse();
        userCreationResponse.setStatus(HttpStatus.OK);
        Mockito.when(userOrchestratorService.updateUser(anyString(),any(UserCreationRequest.class), anyString())).thenReturn(userCreationResponse);
        ResponseEntity<UserCreationResponse> response = controller.updateUser("1",new UserCreationRequest(), traceId);
        Assertions.assertEquals(HttpStatus.OK, Objects.requireNonNull(response.getBody()).getStatus());
    }

    @Test
    public void testDeleteUser(){
        userCreationResponse= new UserCreationResponse();
        userCreationResponse.setStatus(HttpStatus.OK);
        Mockito.when(userOrchestratorService.deactivateUser(anyString(), anyString())).thenReturn(userCreationResponse);
        ResponseEntity<UserCreationResponse> response = controller.deleteUser("1", traceId);
        Assertions.assertEquals(HttpStatus.OK, Objects.requireNonNull(response.getBody()).getStatus());
    }

    @Test
    public void testCreateOrder(){
        orderResponse= new OrderResponse();
        orderResponse.setHttpStatus(HttpStatus.OK);
        Mockito.when(orderOrchestratorService.createOrder(any(OrderRequest.class), anyString())).thenReturn(orderResponse);
        ResponseEntity<OrderResponse> response = controller.createOrder(new OrderRequest(), traceId);
        Assertions.assertEquals(HttpStatus.OK, Objects.requireNonNull(response.getBody()).getHttpStatus());
    }

    @Test
    public void testCreateOrderWithNullResponse(){
        Mockito.when(orderOrchestratorService.createOrder(any(OrderRequest.class), anyString())).thenReturn(null);
        ResponseEntity<OrderResponse> response = controller.createOrder(new OrderRequest(), traceId);
        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testUpdateOrderStatus(){
        orderResponse= new OrderResponse();
        orderResponse.setHttpStatus(HttpStatus.OK);
        Mockito.when(orderOrchestratorService.updateOrderStatus(anyString(),any(OrderStatus.class), anyString())).thenReturn(orderResponse);
        ResponseEntity<OrderResponse> response = controller.updateOrderStatus("1",OrderStatus.PENDING, traceId);
        Assertions.assertEquals(HttpStatus.OK, Objects.requireNonNull(response.getBody()).getHttpStatus());
    }

    @Test
    public void testUpdateOrderStatusWithNullResponse(){
        Mockito.when(orderOrchestratorService.updateOrderStatus(anyString(),any(OrderStatus.class), anyString())).thenReturn(null);
        ResponseEntity<OrderResponse> response = controller.updateOrderStatus("1",OrderStatus.PENDING, traceId);
        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testGetOrderDetails(){
        orderResponse= new OrderResponse();
        orderResponse.setHttpStatus(HttpStatus.OK);
        Mockito.when(orderOrchestratorService.getOrderById(anyString(), anyString())).thenReturn(orderResponse);
        ResponseEntity<OrderResponse> response = controller.getOrderDetails("1", traceId);
        Assertions.assertEquals(HttpStatus.OK, Objects.requireNonNull(response.getBody()).getHttpStatus());
    }

    @Test
    public void testGetOrderDetailsWithNullResponse(){
        Mockito.when(orderOrchestratorService.getOrderById(anyString(), anyString())).thenReturn(null);
        ResponseEntity<OrderResponse> response = controller.getOrderDetails("1", traceId);
        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testGetUserOrderDetails(){
        orderResponseList = new ArrayList<>();
        orderResponse= new OrderResponse();
        orderResponse.setHttpStatus(HttpStatus.OK);
        orderResponseList.add(orderResponse);
        Mockito.when(orderOrchestratorService.getOrderByUserId(anyString(), anyString())).thenReturn(orderResponseList);
        ResponseEntity<List<OrderResponse>> response = controller.getUserOrderDetails("1", traceId);
        Assertions.assertEquals(HttpStatus.OK, Objects.requireNonNull(response.getBody()).get(0).getHttpStatus());
    }

    @Test
    public void testGetUserOrderDetailsWithNullResponse(){
        Mockito.when(orderOrchestratorService.getOrderByUserId(anyString(), anyString())).thenReturn(null);
        ResponseEntity<List<OrderResponse>> response = controller.getUserOrderDetails("1", traceId);
        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testForgotPassword(){
        PasswordResetResponse response = new PasswordResetResponse();
        response.setMessage("test message");
        response.setStatus(HttpStatus.OK);
        Mockito.when(resetService.sendPasswordResetLink(anyString(), anyString())).thenReturn(response);
        ResponseEntity<PasswordResetResponse> passwordResetResponse = controller.forgotPassword("email", traceId);
        Assertions.assertEquals("test message", Objects.requireNonNull(passwordResetResponse.getBody()).getMessage());
    }

    @Test
    public void testResetPassword(){
        PasswordResetResponse response = new PasswordResetResponse();
        response.setMessage("test message");
        response.setStatus(HttpStatus.OK);
        Mockito.when(resetService.resetPassword(anyString(), anyString(), anyString())).thenReturn(response);
        ResponseEntity<PasswordResetResponse> passwordResetResponse = controller.resetPassword("email", "password", traceId);
        Assertions.assertEquals("test message", Objects.requireNonNull(passwordResetResponse.getBody()).getMessage());
    }

    @Test
    public void testLogin(){
        LoginResponse response = new LoginResponse();
        response.setUserId("test user");
        response.setToken("test token");
        response.setStatus(HttpStatus.OK);
        Mockito.when(userOrchestratorService.login(anyString(), anyString(), anyString())).thenReturn(response);
        ResponseEntity<LoginResponse> passwordResetResponse = controller.login("email", "password", traceId);
        Assertions.assertEquals("test token", Objects.requireNonNull(passwordResetResponse.getBody()).getToken());
    }

    @Test
    public void testLogout(){
        LogoutResponse response = new LogoutResponse();
        response.setMessage("test message");
        response.setStatus(HttpStatus.OK);
        Mockito.when(userOrchestratorService.logout(anyString(), anyString())).thenReturn(response);
        ResponseEntity<LogoutResponse> passwordResetResponse = controller.logout("authHeader",  traceId);
        Assertions.assertEquals("test message", Objects.requireNonNull(passwordResetResponse.getBody()).getMessage());
    }

    @Test
    public void testGetUserAddress(){
        UserAddressResponse response = new UserAddressResponse();
        response.setStatus(HttpStatus.OK);
        Mockito.when(orderOrchestratorService.getUserAddress(anyString(), anyString())).thenReturn(response);
        ResponseEntity<UserAddressResponse> userAddressResponse = controller.getUserAddress("1", traceId);
        Assertions.assertEquals(HttpStatus.OK, Objects.requireNonNull(userAddressResponse.getBody()).getStatus());
    }
}