package org.example.service;

import lombok.extern.slf4j.Slf4j;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    CartRepository cartRepository;

    @Autowired
    OrderResponseMapper mapper;

    @Autowired
    EmailService emailService;

    public OrderOrchestratorService(UserRepository userRepository, ArtPieceRepository artPieceRepository, OrderRepository orderRepository,
                                    OrderResponseMapper mapper) {
        this.userRepository = userRepository;
        this.artPieceRepository = artPieceRepository;
        this.orderRepository = orderRepository;
        this.mapper = mapper;
    }

    @Transactional
    public OrderResponse createOrder(OrderRequest request, String traceId){

        log.info("createOrder started, traceId: {}", traceId);
        OrderResponse response;
        UserDetails userDetails = userRepository.findByUserId(Integer.valueOf(request.getUserId()));

        if(userDetails != null){
            Order order = new Order();
            order.setUser(userDetails);
            order.setAddress1(request.getAddress1());
            order.setAddress2(request.getAddress2());
            order.setCity(request.getCity());
            order.setState(request.getState());
            order.setPostalCode(request.getPostalCode());
            order.setPhoneNumber(request.getPhoneNumber());
            order.setOrderDate(LocalDateTime.now());
            order.setOrderItems(new ArrayList<>());

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
                } else {
                    return mapper.mapNoArtPieceFoundResponse(traceId);
                }
            }
            order.setTotalAmount(Double.valueOf(request.getTotalAmount()));
            order.setStatus(OrderStatus.PENDING);
            order.setCreatedAt(LocalDateTime.now());
            order.setModifiedAt(LocalDateTime.now());
            Order savedOrder = orderRepository.save(order);
            cartRepository.deleteByUserId(Integer.valueOf(request.getUserId()));
            sendOrderConfirmationEmail(savedOrder, traceId);
            log.info("createOrder ended, traceId: {}", traceId);
            return convertToDTO(savedOrder);
        } else {
            response = mapper.mapNoUserFoundResponse(traceId);
        }
        return response;
    }

    @Async
    private void sendOrderConfirmationEmail(Order savedOrder, String traceId) {
        log.info("sendOrderConfirmationEmail started, traceId: {}", traceId);

        StringBuilder orderDetails = new StringBuilder();

        for (OrderItem item : savedOrder.getOrderItems()) {
            String imageUrl = item.getArtPiece().getImageUrl();
            String artPieceLink = "http://localhost:3000/pieces/" + item.getArtPiece().getArtId();

            orderDetails.append("<div class='order-item'>")
                    .append("<img src='").append(imageUrl).append("' class='art-piece-img'/>")
                    .append("<p><a href='").append(artPieceLink).append("' class='art-piece-link'>")
                    .append(item.getArtPiece().getTitle())
                    .append("</a></p>")
                    .append("<p>Quantity: ").append(item.getQuantity()).append("</p>")
                    .append("<p>Price: $").append(item.getPrice()).append("</p>")
                    .append("</div>");
        }

        String emailBody =
                "<html>" +
                        "<head>" +
                        "<style>" +
                        "p {font-size: 20px;}" +
                        ".order-item { display: flex; align-items: center; justify-content: space-between; margin-bottom: 10px; }" +
                        ".order-item img { height: 100px; width: 100px; border-radius: 10px; margin-right: 15px; }" +
                        ".order-item div { display: flex; flex-direction: column; justify-content: center; align-items: center; }" +
                        ".order-item p { margin: 5px 0; font-size: 16px; text-align: center; margin-top: 20px; padding: 10px;}" +
                        ".art-piece-link { font-weight: bold; color: #007bff; text-decoration: none; }" +
                        ".art-piece-link:hover { text-decoration: underline; }" +
                        "body { font-size: 16px; line-height: 1.5; }" +
                        "hr { margin: 10px 0; border: 1px solid #ccc; }" +
                        ".address p { margin: 0; padding: 0; font-size: 16px; }" +
                        ".address b { font-size: 18px; }" +
                        "</style>" +
                        "</head>" +
                        "<body>" +
                        "<p>Hey " + savedOrder.getUser().getUserName() + ",</p>" +
                        "<p>Thank you for your order.</p>" +
                        "<p>Here are your order details:</p>" +
                        orderDetails +
                        "<p style='font-size: 20px;'><b>Total Amount: $" + savedOrder.getTotalAmount() + "</b></p>" +
                        "<div class='address'>" +
                        "<p><b>Shipping Address:</b></p>" +
                        "<p>" + savedOrder.getAddress1() + "</p>" +
                        (savedOrder.getAddress2() != null ? "<p>" + savedOrder.getAddress2() + "</p>" : "") +
                        "<p>" + savedOrder.getCity() + ", " +
                        savedOrder.getState() + " " +
                        savedOrder.getPostalCode() + "</p>" +
                        "<p>Phone: " + savedOrder.getPhoneNumber() + "</p>" +
                        "</div>" +
                        "<p>We will notify you once your order is shipped.</p>" +
                        "<p>Regards, <br/>Art Gallery Team</p>" +
                        "</body>" +
                        "</html>";


        emailService.sendEmail(savedOrder.getUser().getEmail(), "Art Gallery Order Confirmation", emailBody, true);
        log.info("sendOrderConfirmationEmail ended, traceId: {}", traceId);
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

    public UserAddressResponse getUserAddress(String userId, String traceId) {
        log.info("getUserAddress started, traceId: {}", traceId);

        UserAddressResponse response;
        Pageable pageable = PageRequest.of(0, 1);
        List<Order> orders = orderRepository.findLatestOrderByUserId(Integer.valueOf(userId), pageable);
        Order order = (orders == null || orders.isEmpty()) ? null : orders.get(0);

        if(order != null){
            response = mapper.mapPreviousAddressResponse(order,traceId);
        } else {
            response = mapper.mapNoPreviousOrderFoundResponse(traceId);
        }

        log.info("getUserAddress ended, traceId: {}", traceId);
        return response;
    }

    private OrderResponse convertToDTO(Order savedOrder) {
        OrderResponse response = new OrderResponse();
        response.setOrderId(String.valueOf(savedOrder.getOrderId()));
        response.setUserId(String.valueOf(savedOrder.getUser().getUserId()));
        response.setOrderDate(String.valueOf(savedOrder.getOrderDate()));
        response.setStatus(savedOrder.getStatus().name());
        response.setTotalAmount(String.valueOf(savedOrder.getTotalAmount()));
        response.setAddress1(savedOrder.getAddress1());
        response.setAddress2(savedOrder.getAddress2());
        response.setCity(savedOrder.getCity());
        response.setState(savedOrder.getState());
        response.setPostalCode(savedOrder.getPostalCode());
        response.setPhoneNumber(savedOrder.getPhoneNumber());
        response.setOrderItems(savedOrder.getOrderItems().stream()
                .map(item -> new OrderItemDetails(
                        item.getArtPiece().getArtId().toString(),
                        item.getPrice().toString(),
                        item.getArtPiece().getTitle(),
                        item.getQuantity().toString(),
                        item.getArtPiece().getImageUrl()
                ))
                .collect(Collectors.toList()));
        return response;
    }
}
