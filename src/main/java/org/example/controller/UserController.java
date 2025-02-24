package org.example.controller;

import lombok.extern.slf4j.Slf4j;
import org.example.enums.OrderStatus;
import org.example.json.*;
import org.example.service.OrderOrchestratorService;
import org.example.service.PasswordResetService;
import org.example.service.PurchaseHistoryOrchestratorService;
import org.example.service.UserOrchestratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@Slf4j
public class UserController {

    @Autowired
    UserOrchestratorService userOrchestratorService;

    @Autowired
    PurchaseHistoryOrchestratorService purchaseHistoryOrchestratorService;

    @Autowired
    OrderOrchestratorService orderOrchestratorService;

    @Autowired
    private PasswordResetService resetService;



    public UserController(UserOrchestratorService userOrchestratorService, PurchaseHistoryOrchestratorService purchaseHistoryOrchestratorService,
                          OrderOrchestratorService orderOrchestratorService, PasswordResetService resetService) {

        this.userOrchestratorService = userOrchestratorService;
        this.purchaseHistoryOrchestratorService = purchaseHistoryOrchestratorService;
        this.orderOrchestratorService = orderOrchestratorService;
        this.resetService = resetService;
    }

//    @GetMapping("/generateKey")
//    public static void main(String[] args) {
//        Key key = Keys.secretKeyFor(SignatureAlgorithm.HS512);
//        String base64Key = Encoders.BASE64.encode(key.getEncoded());
//        System.out.println("Generated Secret Key: " + base64Key);
//    }

    @PostMapping("/signup")
    public ResponseEntity<UserCreationResponse> createUser(@RequestBody UserCreationRequest userCreationRequest,
                                               @RequestHeader(required = false, name = "TraceId") String traceId){
        log.info("Create User Controller started, traceId : {}", traceId);
        UserCreationResponse response = userOrchestratorService.createUser(userCreationRequest, traceId);
        log.info("Create User Controller ended, traceId : {}", traceId);
            return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestParam String username,
                                                     @RequestParam String password,
                                                     @RequestHeader(required = false, name = "TraceId") String traceId) {
        log.info("Login Controller started, traceId : {}", traceId);
        LoginResponse response = userOrchestratorService.login(username, password, traceId);
//        response.put("token", token);
        log.info("Login Controller ended, traceId : {}", traceId);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<LogoutResponse> logout(@RequestHeader("Authorization") String authHeader, @RequestHeader(required = false, name = "TraceId") String traceId) {
        log.info("Log out controller started, traceId : {}", traceId);
        LogoutResponse response =  userOrchestratorService.logout(authHeader, traceId);
        log.info("Log out controller ended, traceId : {}", traceId);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("/user/{userId}/purchasehistory")
    public ResponseEntity<List<PurchaseHistoryResponse>> purchaseHistory(@PathVariable String userId,
                                                                @RequestHeader(required = false, name = "TraceId") String traceId){
        log.info("Purchase history Controller started, traceId : {}", traceId);
        List<PurchaseHistoryResponse> response = purchaseHistoryOrchestratorService.getPurchaseHistory(userId, traceId);
        log.info("Purchase history Controller ended, traceId : {}", traceId);
        if(response.get(0).getStatus().equals(HttpStatus.NOT_FOUND)){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping("/user/{userId}")
    public ResponseEntity<UserCreationResponse> updateUser(@PathVariable String userId,
                                               @RequestBody UserCreationRequest userCreationRequest,
                                               @RequestHeader(required = false, name = "TraceId") String traceId){
        log.info("Update User Controller started, traceId : {}", traceId);
        UserCreationResponse response = userOrchestratorService.updateUser(userId, userCreationRequest, traceId);
        log.info("Update User Controller ended, traceId : {}", traceId);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @DeleteMapping("/user/delete/{userId}")
    public ResponseEntity<UserCreationResponse> deleteUser(@PathVariable String userId,
                                                           @RequestHeader(required = false, name = "TraceId") String traceId){
        log.info("Delete User Controller started, traceId : {}", traceId);
        UserCreationResponse response = userOrchestratorService.deactivateUser(userId, traceId);
        log.info("Delete User Controller ended, traceId : {}", traceId);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PostMapping("/user/create-order")
    public ResponseEntity<OrderResponse> createOrder(@RequestBody OrderRequest orderRequest,
                                                     @RequestHeader(required = false, name = "TraceId") String traceId){
        log.info("createOrder Controller started, traceId : {}", traceId);
        OrderResponse response = orderOrchestratorService.createOrder(orderRequest, traceId);
        log.info("createOrder Controller ended, traceId : {}", traceId);
        if(response != null){
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    @PostMapping("/admin/update-order-status/{orderId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<OrderResponse> updateOrderStatus(@PathVariable String orderId,
                                                           @RequestBody OrderStatus orderStatus,
                                                            @RequestHeader(required = false, name = "TraceId") String traceId){
        log.info("updateOrderStatus Controller started, traceId : {}", traceId);
        OrderResponse response = orderOrchestratorService.updateOrderStatus(orderId, orderStatus, traceId);
        log.info("updateOrderStatus Controller ended, traceId : {}", traceId);
        if(response != null){
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    @GetMapping("/user/order-details/{orderId}")
    public ResponseEntity<OrderResponse> getOrderDetails(@PathVariable String orderId,
                                                         @RequestHeader(required = false, name = "TraceId") String traceId){
        log.info("getOrderDetails Controller started, traceId : {}", traceId);
        OrderResponse response = orderOrchestratorService.getOrderById(orderId,traceId);
        log.info("getOrderDetails Controller ended, traceId : {}", traceId);
        if(response != null){
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    @GetMapping("/user/{userId}/order-details")
    public ResponseEntity<List<OrderResponse>> getUserOrderDetails(@PathVariable String userId,
                                                         @RequestHeader(required = false, name = "TraceId") String traceId){
        log.info("getUserOrderDetails Controller started, traceId : {}", traceId);
        List<OrderResponse> response = orderOrchestratorService.getOrderByUserId(userId,traceId);
        log.info("getUserOrderDetails Controller ended, traceId : {}", traceId);
        if(response != null){
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    @GetMapping("/user/address/{userId}")
    public ResponseEntity<UserAddressResponse> getUserAddress(@PathVariable String userId,
                                                                   @RequestHeader(required = false, name = "TraceId") String traceId){
        log.info("getUserAddress Controller started, traceId : {}", traceId);
        UserAddressResponse response = orderOrchestratorService.getUserAddress(userId,traceId);
        log.info("getUserAddress Controller ended, traceId : {}", traceId);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<PasswordResetResponse> forgotPassword(@RequestParam String email, @RequestHeader(required = false, name = "TraceId") String traceId) {
        log.info("forgotPassword Controller started, traceId : {}", traceId);
        PasswordResetResponse response = resetService.sendPasswordResetLink(email, traceId);
        log.info("forgotPassword Controller ended, traceId : {}", traceId);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<PasswordResetResponse> resetPassword(@RequestParam String token, @RequestParam String newPassword, @RequestHeader(required = false, name = "TraceId") String traceId) {
        log.info("resetPassword Controller started, traceId : {}", traceId);
        PasswordResetResponse response = resetService.resetPassword(token, newPassword, traceId);
        log.info("resetPassword Controller ended, traceId : {}", traceId);
        return ResponseEntity.status(response.getStatus()).body(response);
    }
}
