package org.example.mapper;

import lombok.extern.slf4j.Slf4j;
import org.example.json.Address;
import org.example.json.OrderResponse;
import org.example.json.UserAddressResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.example.entity.Order;

@Component
@Slf4j
public class OrderResponseMapper {

    public OrderResponse mapNoArtPieceFoundResponse(String traceId) {
        log.info("map no art piece found response started, traceId: {}", traceId);
        OrderResponse response = new OrderResponse();
        response.setHttpStatus(HttpStatus.NOT_FOUND);
        response.setMessage("No art piece found");
        log.info("map no art piece found response ended, traceId: {}", traceId);
        return response;
    }

    public OrderResponse mapNoUserFoundResponse(String traceId) {
        log.info("map no user found response started, traceId: {}", traceId);
        OrderResponse response = new OrderResponse();
        response.setHttpStatus(HttpStatus.NOT_FOUND);
        response.setMessage("No user found");
        log.info("map no user found response ended, traceId: {}", traceId);
        return response;
    }

    public OrderResponse mapNoOrderFoundResponse(String traceId) {
        log.info("map no order found response started, traceId: {}", traceId);
        OrderResponse response = new OrderResponse();
        response.setHttpStatus(HttpStatus.NOT_FOUND);
        response.setMessage("No order found");
        log.info("map no order found response ended, traceId: {}", traceId);
        return response;
    }

    public UserAddressResponse mapNoPreviousOrderFoundResponse(String traceId) {
        log.info("map no previous order found response started, traceId: {}", traceId);
        UserAddressResponse response = new UserAddressResponse();
        response.setStatus(HttpStatus.NOT_FOUND);
        response.setMessage("No previous order found");
        log.info("map no previous order found response ended, traceId: {}", traceId);
        return response;
    }

    public UserAddressResponse mapPreviousAddressResponse(Order order, String traceId) {
        log.info("map previous address response started, traceId: {}", traceId);
        UserAddressResponse response = new UserAddressResponse();
        Address address = new Address();
        address.setAddress1(order.getAddress1());
        address.setAddress2(order.getAddress2());
        address.setCity(order.getCity());
        address.setState(order.getState());
        address.setPostalCode(order.getPostalCode());
        address.setPhoneNumber(order.getPhoneNumber());
        address.setName(order.getUser().getUserName());
        response.setAddress(address);
        response.setStatus(HttpStatus.OK);
        response.setMessage("Success");
        log.info("map previous address response ended, traceId: {}", traceId);
        return response;
    }
}
