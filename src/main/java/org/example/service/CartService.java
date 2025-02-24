package org.example.service;

import com.fasterxml.jackson.databind.deser.std.StringArrayDeserializer;
import lombok.extern.slf4j.Slf4j;
import org.example.entity.ArtPiece;
import org.example.entity.Cart;
import org.example.entity.UserDetails;
import org.example.json.CartItem;
import org.example.json.CartResponse;
import org.example.repository.ArtPieceRepository;
import org.example.repository.CartRepository;
import org.example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ArtPieceRepository artPieceRepository;

    @Transactional
    public void syncCart(Integer userId, List<CartItem> cartItems, String traceId) {
        log.info("Sync Cart Service started, traceId : {}", traceId);
        UserDetails user = userRepository.findByUserId(userId);
        if(user != null) {
            cartRepository.deleteByUserId(userId);
            for (CartItem cartItem : cartItems) {
                ArtPiece artPiece = artPieceRepository.findByArtId(cartItem.getArtId());
                if(artPiece != null) {
                    Cart cart = new Cart();
                    cart.setUser(user);
                    cart.setArtPiece(artPiece);
                    cart.setQuantity(cartItem.getQuantity());
                    cartRepository.save(cart);
                } else {
                    throw new RuntimeException("No art found");
                }
            }
        } else {
            throw new RuntimeException("No user found");
        }
        log.info("Sync Cart Service ended, traceId : {}", traceId);
    }

    public List<CartResponse> getCartForUser(Integer userId, String traceId) {
        log.info("getCartForUser Service started, traceId : {}", traceId);
        List<Cart> cartItems = cartRepository.findByUserId(userId);
        List<CartResponse> responses ;
        if(cartItems != null && !cartItems.isEmpty()) {
            responses = new ArrayList<>();
            CartResponse response;
            for (Cart cartItem : cartItems) {
                response = new CartResponse();
                response.setCartId(Math.toIntExact(cartItem.getCartId()));
                response.setArtId(cartItem.getArtPiece().getArtId());
                response.setArtName(cartItem.getArtPiece().getTitle());
                response.setArtCategory(cartItem.getArtPiece().getCategory().getName());
                response.setArtPrice(cartItem.getArtPiece().getPrice());
                response.setQuantity((cartItem.getQuantity()));
                response.setImageUrl(cartItem.getArtPiece().getImageUrl());
                responses.add(response);
            }
        } else {
            responses = new ArrayList<>();
            CartResponse response = new CartResponse();
            response.setStatus(HttpStatus.NOT_FOUND);
            responses.add(response);
        }
        log.info("getCartForUser Service started, traceId : {}", traceId);
        return responses;
    }
}