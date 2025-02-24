package org.example.controller;

import org.example.entity.Cart;
import org.example.json.*;
import org.example.service.ArtCategoryOrchestratorService;
import org.example.service.ArtPieceOrchestratorService;
import org.example.service.CartService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import static org.mockito.ArgumentMatchers.*;

@ExtendWith(MockitoExtension.class)
class ArtControllerTest {


    @Mock
    ArtCategoryOrchestratorService artCategoryOrchestratorService;

    @Mock
    ArtPieceOrchestratorService artPieceOrchestratorService;

    @Mock
    CartService cartService;

    @InjectMocks
    ArtController artController;
    List<ArtCategoryResponse> artCategoryResponses;
    ArtCategoryResponse artCategoryResponse;
    List<ArtPieceResponse> artPieceResponses;
    ArtPieceResponse artPieceResponse;

    String traceId = UUID.randomUUID().toString();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Initializes @Mock annotated fields
    }

    @Test
    public void testInsertArtCategory(){
        artCategoryResponse = new ArtCategoryResponse();
        artCategoryResponse.setStatus(HttpStatus.OK);
        Mockito.when(artCategoryOrchestratorService.insertArtCategory(any(ArtCategoryRequest.class),anyString())).thenReturn(artCategoryResponse);
        ResponseEntity<ArtCategoryResponse> response = artController.insertArtCategory(new ArtCategoryRequest(), traceId);
        Assertions.assertEquals(HttpStatus.OK, Objects.requireNonNull(response.getBody()).getStatus());
    }

    @Test
    public void testUpdateArtCategory(){
        artCategoryResponse = new ArtCategoryResponse();
        artCategoryResponse.setStatus(HttpStatus.OK);
        Mockito.when(artCategoryOrchestratorService.updateArtCategory(any(ArtCategoryRequest.class),anyString())).thenReturn(artCategoryResponse);
        ResponseEntity<ArtCategoryResponse> response = artController.updateArtCategory(new ArtCategoryRequest(), traceId);
        Assertions.assertEquals(HttpStatus.OK, Objects.requireNonNull(response.getBody()).getStatus());
    }

    @Test
    public void testGetArtCategory(){
        artCategoryResponses = new ArrayList<>();
        artCategoryResponse = new ArtCategoryResponse();
        artCategoryResponse.setStatus(HttpStatus.OK);
        artCategoryResponses.add(artCategoryResponse);
        Mockito.when(artCategoryOrchestratorService.getArtCategories(anyString())).thenReturn(artCategoryResponses);
        ResponseEntity<List<ArtCategoryResponse>> response = artController.getArtCategories(traceId);
        Assertions.assertEquals(HttpStatus.OK, Objects.requireNonNull(response.getBody()).get(0).getStatus());
    }

    @Test
    public void testGetArtCategoryWithNullResponse(){
        Mockito.when(artCategoryOrchestratorService.getArtCategories(anyString())).thenReturn(null);
        ResponseEntity<List<ArtCategoryResponse>> response = artController.getArtCategories(traceId);
        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testGetArtCategoryWithEmptyResponse(){
        artCategoryResponses = new ArrayList<>();
        artCategoryResponse = new ArtCategoryResponse();
        Mockito.when(artCategoryOrchestratorService.getArtCategories(anyString())).thenReturn(artCategoryResponses);
        ResponseEntity<List<ArtCategoryResponse>> response = artController.getArtCategories(traceId);
        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testGetArtCategoryWithStatusCodeResponse(){
        artCategoryResponses = new ArrayList<>();
        artCategoryResponse = new ArtCategoryResponse();
        artCategoryResponse.setStatus(HttpStatus.NOT_FOUND);
        artCategoryResponses.add(artCategoryResponse);
        Mockito.when(artCategoryOrchestratorService.getArtCategories(anyString())).thenReturn(artCategoryResponses);
        ResponseEntity<List<ArtCategoryResponse>> response = artController.getArtCategories(traceId);
        Assertions.assertEquals(HttpStatus.NOT_FOUND, Objects.requireNonNull(response.getBody()).get(0).getStatus());
    }

    @Test
    public void testInsertArtPiece(){
        artPieceResponse = new ArtPieceResponse();
        artPieceResponse.setStatus(HttpStatus.OK);
        Mockito.when(artPieceOrchestratorService.insertArtPiece(any(ArtPieceRequest.class),anyString())).thenReturn(artPieceResponse);
        ResponseEntity<ArtPieceResponse> response = artController.insertArtPiece(new ArtPieceRequest(), traceId);
        Assertions.assertEquals(HttpStatus.OK, Objects.requireNonNull(response.getBody()).getStatus());
    }

    @Test
    public void testUpdateArtPiece(){
        artPieceResponse = new ArtPieceResponse();
        artPieceResponse.setStatus(HttpStatus.OK);
        Mockito.when(artPieceOrchestratorService.updateArtPiece(any(ArtPieceRequest.class),anyString())).thenReturn(artPieceResponse);
        ResponseEntity<ArtPieceResponse> response = artController.updateArtPiece(new ArtPieceRequest(), traceId);
        Assertions.assertEquals(HttpStatus.OK, Objects.requireNonNull(response.getBody()).getStatus());
    }

    @Test
    public void testGetArtPieces(){
        artPieceResponses = new ArrayList<>();
        artPieceResponse = new ArtPieceResponse();
        artPieceResponse.setStatus(HttpStatus.OK);
        artPieceResponses.add(artPieceResponse);
        Mockito.when(artPieceOrchestratorService.getArtPieces(anyString(),anyString())).thenReturn(artPieceResponses);
        ResponseEntity<List<ArtPieceResponse>> response = artController.getArtPieces("1",traceId);
        Assertions.assertEquals(HttpStatus.OK, Objects.requireNonNull(response.getBody()).get(0).getStatus());
    }

    @Test
    public void testGetArtPiecesWithNullResponse(){
        Mockito.when(artPieceOrchestratorService.getArtPieces(anyString(),anyString())).thenReturn(null);
        ResponseEntity<List<ArtPieceResponse>> response = artController.getArtPieces("1",traceId);
        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testGetArtPiece(){
        artPieceResponse = new ArtPieceResponse();
        artPieceResponse.setStatus(HttpStatus.OK);
        Mockito.when(artPieceOrchestratorService.getArtPiece(anyString(),anyString())).thenReturn(artPieceResponse);
        ResponseEntity<ArtPieceResponse> response = artController.getArtPiece("1",traceId);
        Assertions.assertEquals(HttpStatus.OK, Objects.requireNonNull(response.getBody()).getStatus());
    }

    @Test
    public void testGetArtPieceWithNullResponse(){
        Mockito.when(artPieceOrchestratorService.getArtPiece(anyString(),anyString())).thenReturn(null);
        ResponseEntity<ArtPieceResponse> response = artController.getArtPiece("1",traceId);
        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testDeleteArtPiece(){
        artPieceResponse = new ArtPieceResponse();
        artPieceResponse.setStatus(HttpStatus.OK);
        Mockito.when(artPieceOrchestratorService.deleteArtPiece(anyString(),anyString())).thenReturn(artPieceResponse);
        ResponseEntity<ArtPieceResponse> response = artController.deleteArtPiece("1",traceId);
        Assertions.assertEquals(HttpStatus.OK, Objects.requireNonNull(response.getBody()).getStatus());
    }

    @Test
    public void testDeleteArtCategory(){
        artCategoryResponse = new ArtCategoryResponse();
        artCategoryResponse.setStatus(HttpStatus.OK);
        Mockito.when(artCategoryOrchestratorService.deleteArtCategory(anyString(),anyString())).thenReturn(artCategoryResponse);
        ResponseEntity<ArtCategoryResponse> response = artController.deleteArtCategory("1", traceId);
        Assertions.assertEquals(HttpStatus.OK, Objects.requireNonNull(response.getBody()).getStatus());
    }

    @Test
    public void testGetUserCart(){
        List<CartResponse> responses = new ArrayList<>();
        CartResponse  cartResponse= new CartResponse();
        cartResponse.setArtName("test art");
        responses.add(cartResponse);
        Mockito.when(cartService.getCartForUser(anyInt(),anyString())).thenReturn(responses);
        ResponseEntity<List<CartResponse>> response = artController.getUserCart(1, traceId);
        Assertions.assertEquals("test art", Objects.requireNonNull(response.getBody()).get(0).getArtName());
    }

    @Test
    public void testGetUserCartWithNullResponse(){
        Mockito.when(cartService.getCartForUser(anyInt(),anyString())).thenReturn(null);
        ResponseEntity<List<CartResponse>> response = artController.getUserCart(1, traceId);
        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testSyncCart() {
        List<CartItem> cartItems = new ArrayList<>();

        Mockito.doNothing().when(cartService).syncCart(anyInt(), anyList(), anyString());
        ResponseEntity<Void> response = artController.syncCart(1, cartItems, traceId);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());

        Mockito.verify(cartService).syncCart(eq(1), eq(cartItems), eq(traceId));
    }

    @Test
    public void testUploadImageWithSuccess() throws Exception {
        MockMultipartFile multipartFile = new MockMultipartFile(
                "image.jpg",
                "image.jpg",
                "image/jpeg",
                "test image content".getBytes(StandardCharsets.UTF_8)
        );
        Mockito.when(artPieceOrchestratorService.uploadImageAndSetUrl(anyInt(), any(MultipartFile.class))).thenReturn("https://image.jpg");
        ResponseEntity<String> response = artController.uploadImage(1,multipartFile);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testUploadImageWithNullResponse() throws Exception {
        MockMultipartFile multipartFile = new MockMultipartFile(
                "image.jpg",
                "image.jpg",
                "image/jpeg",
                "test image content".getBytes(StandardCharsets.UTF_8)
        );
        Mockito.when(artPieceOrchestratorService.uploadImageAndSetUrl(anyInt(), any(MultipartFile.class))).thenReturn(null);
        ResponseEntity<String> response = artController.uploadImage(1,multipartFile);
        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testUploadImageWithFailedToUpload() throws Exception {
        MockMultipartFile multipartFile = new MockMultipartFile(
                "image.jpg",
                "image.jpg",
                "image/jpeg",
                "test image content".getBytes(StandardCharsets.UTF_8)
        );

        Mockito.when(artPieceOrchestratorService.uploadImageAndSetUrl(1, multipartFile)).thenThrow(new IOException("Image upload failed!"));
        ResponseEntity<String> response = artController.uploadImage(1,multipartFile);
        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testUploadImageWithRuntimeException() throws Exception {
        MockMultipartFile multipartFile = new MockMultipartFile(
                "image.jpg",
                "image.jpg",
                "image/jpeg",
                "test image content".getBytes(StandardCharsets.UTF_8)
        );

        Mockito.when(artPieceOrchestratorService.uploadImageAndSetUrl(1, multipartFile)).thenThrow(new RuntimeException());
        ResponseEntity<String> response = artController.uploadImage(1,multipartFile);
        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

}