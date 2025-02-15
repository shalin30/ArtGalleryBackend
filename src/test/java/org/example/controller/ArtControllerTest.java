package org.example.controller;

import org.example.json.ArtCategoryRequest;
import org.example.json.ArtCategoryResponse;
import org.example.json.ArtPieceRequest;
import org.example.json.ArtPieceResponse;
import org.example.service.ArtCategoryOrchestratorService;
import org.example.service.ArtPieceOrchestratorService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

@ExtendWith(MockitoExtension.class)
class ArtControllerTest {

    @Mock
    ArtCategoryOrchestratorService artCategoryOrchestratorService;

    @Mock
    ArtPieceOrchestratorService artPieceOrchestratorService;

    @InjectMocks
    ArtController artController;
    List<ArtCategoryResponse> artCategoryResponses;
    ArtCategoryResponse artCategoryResponse;
    List<ArtPieceResponse> artPieceResponses;
    ArtPieceResponse artPieceResponse;

    String traceId = UUID.randomUUID().toString();

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

}