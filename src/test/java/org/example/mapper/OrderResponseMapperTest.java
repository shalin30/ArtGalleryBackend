package org.example.mapper;

import org.example.json.OrderResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.UUID;


@ExtendWith(MockitoExtension.class)
class OrderResponseMapperTest {

    @InjectMocks
    OrderResponseMapper mapper;

    String traceId = UUID.randomUUID().toString();

    @Test
    public void testMapNoArtPieceFoundResponse(){
        OrderResponse response = mapper.mapNoArtPieceFoundResponse(traceId);
        Assertions.assertEquals("No art piece found", response.getMessage());
    }

    @Test
    public void testMapNoUserFoundResponse(){
        OrderResponse response = mapper.mapNoUserFoundResponse(traceId);
        Assertions.assertEquals("No user found", response.getMessage());
    }

    @Test
    public void testMapNoOrderFoundResponse(){
        OrderResponse response = mapper.mapNoOrderFoundResponse(traceId);
        Assertions.assertEquals("No order found", response.getMessage());
    }
}