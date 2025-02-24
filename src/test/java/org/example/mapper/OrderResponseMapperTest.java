package org.example.mapper;

import org.example.entity.Order;
import org.example.entity.UserDetails;
import org.example.json.Address;
import org.example.json.OrderResponse;
import org.example.json.UserAddressResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

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

    @Test
    public void testMapNoPreviousOrderFoundResponse(){
        UserAddressResponse response = mapper.mapNoPreviousOrderFoundResponse(traceId);
        Assertions.assertEquals("No previous order found", response.getMessage());
    }

    @Test
    public void testMapPreviousAddressResponse(){
        Order order = new Order();
        order.setAddress1("address 1");
        order.setAddress2("address 2");
        order.setCity("city");
        order.setState("state");
        order.setPostalCode("123456");
        order.setPhoneNumber("1234567890");
        UserDetails userDetails = new UserDetails();
        userDetails.setUserName("name");
        order.setUser(userDetails);
        UserAddressResponse response = mapper.mapPreviousAddressResponse(order,traceId);
        Assertions.assertEquals(HttpStatus.OK, response.getStatus());
    }
}