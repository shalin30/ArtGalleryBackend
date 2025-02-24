package org.example.service;

import org.example.entity.ArtPiece;
import org.example.entity.Cart;
import org.example.entity.Categories;
import org.example.entity.UserDetails;
import org.example.json.CartItem;
import org.example.json.CartResponse;
import org.example.repository.ArtPieceRepository;
import org.example.repository.CartRepository;
import org.example.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartServiceTest {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ArtPieceRepository artPieceRepository;

    @InjectMocks
    private CartService cartService;
    String traceId = UUID.randomUUID().toString();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void syncCartWithSuccessfulSync() {
        Integer userId = 1;
        List<CartItem> cartItems = new ArrayList<>();
        CartItem cartItem = new CartItem();
        cartItem.setQuantity(1);
        cartItem.setArtId(1);
        cartItems.add(cartItem);

        UserDetails user = new UserDetails();
        user.setUserId(userId);

        ArtPiece artPiece1 = new ArtPiece();
        artPiece1.setArtId(1);

        when(userRepository.findByUserId(userId)).thenReturn(user);
        when(artPieceRepository.findByArtId(1)).thenReturn(artPiece1);

        cartService.syncCart(userId, cartItems, traceId);

        verify(cartRepository, times(1)).deleteByUserId(userId);
        verify(cartRepository, times(1)).save(any(Cart.class));
        verify(artPieceRepository, times(1)).findByArtId(anyInt());
    }

    @Test
    void syncCartWithNullUser() {
        Integer userId = 1;
        List<CartItem> cartItems = new ArrayList<>();
        CartItem cartItem = new CartItem();
        cartItem.setQuantity(1);
        cartItem.setArtId(1);
        cartItems.add(cartItem);

        ArtPiece artPiece1 = new ArtPiece();
        artPiece1.setArtId(1);

        when(userRepository.findByUserId(userId)).thenReturn(null);
        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                cartService.syncCart(userId, cartItems, traceId)
        );

        assertEquals("No user found", exception.getMessage());
    }

    @Test
    void syncCartWithNullArt() {
        Integer userId = 1;
        List<CartItem> cartItems = new ArrayList<>();
        CartItem cartItem = new CartItem();
        cartItem.setQuantity(1);
        cartItem.setArtId(1);
        cartItems.add(cartItem);

        UserDetails user = new UserDetails();
        user.setUserId(userId);

        when(userRepository.findByUserId(userId)).thenReturn(user);
        when(artPieceRepository.findByArtId(1)).thenReturn(null);
        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                cartService.syncCart(userId, cartItems, traceId)
        );

        assertEquals("No art found", exception.getMessage());
    }

    @Test
    void getCartForUserWithItems() {
        Integer userId = 1;

        List<Cart> cartItems = new ArrayList<>();
        Cart cart = new Cart();

        Categories category = new Categories();
        category.setName("test name");

        ArtPiece artPiece = new ArtPiece();
        artPiece.setArtId(1);
        artPiece.setTitle("test title");
        artPiece.setPrice(10.00);
        artPiece.setImageUrl("https://test.jpg");
        artPiece.setCategory(category);
        cart.setCartId(1L);
        cart.setQuantity(1);
        cart.setArtPiece(artPiece);
        cartItems.add(cart);

        when(cartRepository.findByUserId(userId)).thenReturn(cartItems);

        List<CartResponse> result = cartService.getCartForUser(userId, traceId);

        assertEquals(1, result.get(0).getArtId());
    }

    @Test
    void getCartForUserWithNullItems() {
        Integer userId = 1;
        when(cartRepository.findByUserId(userId)).thenReturn(null);
        List<CartResponse> result = cartService.getCartForUser(userId, traceId);
        assertEquals(HttpStatus.NOT_FOUND, result.get(0).getStatus());
    }

    @Test
    void getCartForUserWithEmptyItems() {
        Integer userId = 1;
        List<Cart> cartItems = new ArrayList<>();
        when(cartRepository.findByUserId(userId)).thenReturn(cartItems);
        List<CartResponse> result = cartService.getCartForUser(userId, traceId);
        assertEquals(HttpStatus.NOT_FOUND, result.get(0).getStatus());
    }

}