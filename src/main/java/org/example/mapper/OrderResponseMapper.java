package org.example.mapper;

import lombok.extern.slf4j.Slf4j;
import org.example.json.OrderResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

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
}
