package org.example.service;

import lombok.extern.slf4j.Slf4j;
import org.example.entity.ArtPiece;
import org.example.entity.Order;
import org.example.entity.OrderItem;
import org.example.entity.UserDetails;
import org.example.enums.OrderStatus;
import org.example.json.OrderItemDetails;
import org.example.json.OrderItemRequest;
import org.example.json.OrderRequest;
import org.example.json.OrderResponse;
import org.example.mapper.OrderResponseMapper;
import org.example.repository.ArtPieceRepository;
import org.example.repository.OrderRepository;
import org.example.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class OrderOrchestratorService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    ArtPieceRepository artPieceRepository;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    OrderResponseMapper mapper;

    public OrderOrchestratorService(UserRepository userRepository, ArtPieceRepository artPieceRepository, OrderRepository orderRepository,
                                    OrderResponseMapper mapper) {
        this.userRepository = userRepository;
        this.artPieceRepository = artPieceRepository;
        this.orderRepository = orderRepository;
        this.mapper = mapper;
    }

    public OrderResponse createOrder(OrderRequest request, String traceId){

        log.info("createOrder started, traceId: {}", traceId);
        OrderResponse response;
        UserDetails userDetails = userRepository.findByUserId(Integer.valueOf(request.getUserId()));

        if(userDetails != null){
            Order order = new Order();
            order.setUser(userDetails);
            order.setOrderDate(LocalDateTime.now());
            order.setOrderItems(new ArrayList<>());
            double totalAmount = 0.0;

            for(OrderItemRequest artPieceIdDetail : request.getArtPieceDetails()){
                ArtPiece artPiece = artPieceRepository.findByArtId(Integer.valueOf(artPieceIdDetail.getArtPieceId()));
                if(artPiece != null){
                    OrderItem orderItem = new OrderItem();
                    orderItem.setArtPiece(artPiece);
                    orderItem.setPrice(artPiece.getPrice());
                    orderItem.setOrder(order);
                    order.getOrderItems().add(orderItem);
                    orderItem.setQuantity(Integer.valueOf(artPieceIdDetail.getQuantity()));
                    orderItem.setCreatedAt(LocalDateTime.now());
                    orderItem.setModifiedAt(LocalDateTime.now());
                    totalAmount += artPiece.getPrice();
                } else {
                    return mapper.mapNoArtPieceFoundResponse(traceId);
                }
            }
            order.setTotalAmount(totalAmount);
            order.setStatus(OrderStatus.PENDING);
            order.setCreatedAt(LocalDateTime.now());
            order.setModifiedAt(LocalDateTime.now());
            Order savedOrder = orderRepository.save(order);
            log.info("createOrder ended, traceId: {}", traceId);
            return convertToDTO(savedOrder);
        } else {
            response = mapper.mapNoUserFoundResponse(traceId);
        }
        return response;
    }

    public OrderResponse updateOrderStatus(String orderId, OrderStatus status, String traceId){
        log.info("updateOrderStatus started, traceId: {}", traceId);
        OrderResponse response;
        Order order = orderRepository.findByOrderId(Integer.valueOf(orderId));
        if(order != null){
            order.setStatus(status);
            order.setModifiedAt(LocalDateTime.now());
            Order updatedOrder = orderRepository.save(order);
            log.info("updateOrderStatus ended, traceId: {}", traceId);
            return convertToDTO(updatedOrder);
        } else {
            response = mapper.mapNoOrderFoundResponse(traceId);
        }
        log.info("updateOrderStatus ended, traceId: {}", traceId);
        return response;
    }

    public OrderResponse getOrderById(String orderId, String traceId){
        log.info("getOrderById started, traceId: {}", traceId);
        OrderResponse response;
        Order order = orderRepository.findByOrderId(Integer.valueOf(orderId));
        if(order != null){
            log.info("getOrderById ended, traceId: {}", traceId);
            return convertToDTO(order);
        } else{
            response = mapper.mapNoOrderFoundResponse(traceId);
        }
        log.info("getOrderById ended, traceId: {}", traceId);
        return response;
    }

    public List<OrderResponse> getOrderByUserId(String userId, String traceId) {
        log.info("getOrderByUserId started, traceId: {}", traceId);
        List<OrderResponse> orderResponses = new ArrayList<>();
        OrderResponse response = new OrderResponse();
        UserDetails userDetails = userRepository.findByUserId(Integer.valueOf(userId));
        if(userDetails != null) {
            List<Order> orders = orderRepository.findOrdersByUserId(Integer.valueOf(userId));

            if(orders != null && !orders.isEmpty()) {
                log.info("getOrderByUserId started, traceId: {}", traceId);
                return orders.stream().map(this::convertToDTO).collect(Collectors.toList());
            } else {
                response.setHttpStatus(HttpStatus.NOT_FOUND);
                orderResponses.add(response);
            }
        } else {
            response.setHttpStatus(HttpStatus.NOT_FOUND);
            orderResponses.add(response);
        }
        log.info("getOrderByUserId started, traceId: {}", traceId);
        return orderResponses;
    }

    private OrderResponse convertToDTO(Order savedOrder) {
        OrderResponse response = new OrderResponse();
        response.setOrderId(String.valueOf(savedOrder.getOrderId()));
        response.setUserId(String.valueOf(savedOrder.getUser().getUserId()));
        response.setOrderDate(String.valueOf(savedOrder.getOrderDate()));
        response.setStatus(savedOrder.getStatus().name());
        response.setTotalAmount(String.valueOf(savedOrder.getTotalAmount()));
        response.setOrderItems(savedOrder.getOrderItems().stream()
                .map(item -> new OrderItemDetails(
                        item.getArtPiece().getArtId().toString(),
                        item.getPrice().toString()
                ))
                .collect(Collectors.toList()));
        return response;
    }
}
