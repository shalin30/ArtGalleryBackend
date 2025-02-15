package org.example.service;

import org.example.entity.ArtPiece;
import org.example.entity.Categories;
import org.example.json.*;
import org.example.mapper.ArtPieceResponseMapper;
import org.example.repository.ArtCategoryRepository;
import org.example.repository.ArtPieceRepository;
import org.example.repository.OrderItemsRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import static org.mockito.ArgumentMatchers.*;

@ExtendWith(MockitoExtension.class)
class ArtPieceOrchestratorServiceTest {

    @Mock
    ArtPieceRepository artPieceRepository;

    @Mock
    ArtCategoryRepository artCategoryRepository;

    @Mock
    OrderItemsRepository orderItemsRepository;

    @Mock
    ArtPieceFieldValidationService artPieceFieldValidationService;

    @Mock
    ArtPieceResponseMapper mapper;

    @InjectMocks
    ArtPieceOrchestratorService artPieceOrchestratorService;
    String traceId = UUID.randomUUID().toString();

    @Test
    public void testInsertArtPiece(){
        ArtPieceResponse artPieceResponse = new ArtPieceResponse();
        artPieceResponse.setMessage("Success");
        Mockito.when(artPieceFieldValidationService.artPieceFieldValidation(any(ArtPieceRequest.class), anyString())).thenReturn(new ValidationResponse());
        Mockito.when(artCategoryRepository.findByCategoryId(anyInt())).thenReturn(new Categories());
        Mockito.when(artPieceRepository.save(Mockito.any(ArtPiece.class))).thenReturn(null);
        Mockito.when(mapper.mapSuccessResponse(any(ArtPiece.class), anyString())).thenReturn(artPieceResponse);
        ArtPieceResponse response = artPieceOrchestratorService.insertArtPiece(getArtPiece(), traceId);
        Assertions.assertEquals("Success", response.getMessage());
    }

    @Test
    public void testInsertArtPieceWithNullCategory(){
        ArtPieceResponse artPieceResponse = new ArtPieceResponse();
        artPieceResponse.setMessage("no category found");
        Mockito.when(artPieceFieldValidationService.artPieceFieldValidation(any(ArtPieceRequest.class), anyString())).thenReturn(new ValidationResponse());
        Mockito.when(artCategoryRepository.findByCategoryId(anyInt())).thenReturn(null);
        Mockito.when(mapper.mapNoCategoryFoundResponse(anyString())).thenReturn(artPieceResponse);
        ArtPieceResponse response = artPieceOrchestratorService.insertArtPiece(getArtPiece(), traceId);
        Assertions.assertEquals("no category found", response.getMessage());
    }

    @Test
    public void testInsertArtPieceWithValidationError(){
        ArtPieceResponse artCategoryResponse = new ArtPieceResponse();
        ValidationResponse validationResponse = getValidationResponse();
        artCategoryResponse.setValidationResponse(validationResponse.toString());
        Mockito.when(artPieceFieldValidationService.artPieceFieldValidation(any(ArtPieceRequest.class), anyString())).thenReturn(validationResponse);
        Mockito.when(mapper.mapValidationErrorResponse(any(ValidationResponse.class), anyString())).thenReturn(artCategoryResponse);
        ArtPieceResponse response = artPieceOrchestratorService.insertArtPiece(getArtPiece(), traceId);
        Assertions.assertTrue(response.getValidationResponse().contains("Failure"));
    }

    @Test
    public void testUpdateArtPiece(){
        ArtPieceResponse artPieceResponse = new ArtPieceResponse();
        artPieceResponse.setMessage("Success");
        Mockito.when(artPieceFieldValidationService.updateArtPieceFieldValidation(any(ArtPieceRequest.class), anyString())).thenReturn(new ValidationResponse());
        Mockito.when(artPieceRepository.findByArtId(anyInt())).thenReturn(new ArtPiece());
        Mockito.when(artCategoryRepository.findByCategoryId(anyInt())).thenReturn(new Categories());
        Mockito.when(artPieceRepository.save(Mockito.any(ArtPiece.class))).thenReturn(null);
        Mockito.when(mapper.mapUpdateSuccessResponse(any(ArtPiece.class), anyString())).thenReturn(artPieceResponse);
        ArtPieceResponse response = artPieceOrchestratorService.updateArtPiece(getArtPiece(), traceId);
        Assertions.assertEquals("Success", response.getMessage());
    }

    @Test
    public void testUpdateArtPieceWithNullValues(){

        ArtPieceRequest artPieceRequest = new ArtPieceRequest();
        artPieceRequest.setArtId("1");
        artPieceRequest.setTitle(null);
        artPieceRequest.setDescription(null);
        artPieceRequest.setPrice(null);
        artPieceRequest.setCategoryId(null);
        artPieceRequest.setImageUrl(null);

        ArtPieceResponse artPieceResponse = new ArtPieceResponse();
        artPieceResponse.setMessage("Success");
        Mockito.when(artPieceFieldValidationService.updateArtPieceFieldValidation(any(ArtPieceRequest.class), anyString())).thenReturn(new ValidationResponse());
        Mockito.when(artPieceRepository.findByArtId(anyInt())).thenReturn(new ArtPiece());
        Mockito.when(artPieceRepository.save(Mockito.any(ArtPiece.class))).thenReturn(null);
        Mockito.when(mapper.mapUpdateSuccessResponse(any(ArtPiece.class), anyString())).thenReturn(artPieceResponse);
        ArtPieceResponse response = artPieceOrchestratorService.updateArtPiece(artPieceRequest, traceId);
        Assertions.assertEquals("Success", response.getMessage());
    }

    @Test
    public void testUpdateArtPieceWithNullCategory(){
        ArtPieceResponse artPieceResponse = new ArtPieceResponse();
        artPieceResponse.setMessage("no category found");
        Mockito.when(artPieceFieldValidationService.updateArtPieceFieldValidation(any(ArtPieceRequest.class), anyString())).thenReturn(new ValidationResponse());
        Mockito.when(artPieceRepository.findByArtId(anyInt())).thenReturn(new ArtPiece());
        Mockito.when(artCategoryRepository.findByCategoryId(anyInt())).thenReturn(null);
        Mockito.when(mapper.mapNoCategoryFoundResponse(anyString())).thenReturn(artPieceResponse);
        ArtPieceResponse response = artPieceOrchestratorService.updateArtPiece(getArtPiece(), traceId);
        Assertions.assertEquals("no category found", response.getMessage());
    }

    @Test
    public void testUpdateArtPieceWithNullArtPiece(){
        ArtPieceResponse artPieceResponse = new ArtPieceResponse();
        artPieceResponse.setMessage("no art piece found");
        Mockito.when(artPieceFieldValidationService.updateArtPieceFieldValidation(any(ArtPieceRequest.class), anyString())).thenReturn(new ValidationResponse());
        Mockito.when(artPieceRepository.findByArtId(anyInt())).thenReturn(null);
        Mockito.when(mapper.mapNoArtPieceFoundResponse(anyString())).thenReturn(artPieceResponse);
        ArtPieceResponse response = artPieceOrchestratorService.updateArtPiece(getArtPiece(), traceId);
        Assertions.assertEquals("no art piece found", response.getMessage());
    }

    @Test
    public void testUpdateArtPieceWithValidationError(){
        ArtPieceResponse artCategoryResponse = new ArtPieceResponse();
        ValidationResponse validationResponse = getValidationResponse();
        artCategoryResponse.setValidationResponse(validationResponse.toString());
        Mockito.when(artPieceFieldValidationService.updateArtPieceFieldValidation(any(ArtPieceRequest.class), anyString())).thenReturn(validationResponse);
        Mockito.when(mapper.mapValidationErrorResponse(any(ValidationResponse.class), anyString())).thenReturn(artCategoryResponse);
        ArtPieceResponse response = artPieceOrchestratorService.updateArtPiece(getArtPiece(), traceId);
        Assertions.assertTrue(response.getValidationResponse().contains("Failure"));
    }

    @Test
    public void testGetArtPieces(){
        List<ArtPiece> artPieces = new ArrayList<>();
        ArtPiece artPiece = getPiece();
        artPieces.add(artPiece);
        Mockito.when(artPieceRepository.findArtPiecesByCategoryId(anyInt())).thenReturn(artPieces);
        List<ArtPieceResponse> responses = artPieceOrchestratorService.getArtPieces("1", traceId);
        Assertions.assertEquals("1", responses.get(0).getArtId());
    }

    @Test
    public void testGetArtPiece(){
        ArtPiece artPiece = getPiece();
        Mockito.when(artPieceRepository.findByArtId(anyInt())).thenReturn(artPiece);
        ArtPieceResponse responses = artPieceOrchestratorService.getArtPiece("1", traceId);
        Assertions.assertEquals("1", responses.getArtId());
    }

    @Test
    public void testGetArtPiecesWithNullValues(){
        Mockito.when(artPieceRepository.findArtPiecesByCategoryId(anyInt())).thenReturn(null);
        List<ArtPieceResponse> responses = artPieceOrchestratorService.getArtPieces("1", traceId);
        Assertions.assertEquals(HttpStatus.NOT_FOUND, responses.get(0).getStatus());
    }

    @Test
    public void testGetArtPieceWithNullValues(){
        ArtPieceResponse response = new ArtPieceResponse();
        response.setStatus(HttpStatus.NOT_FOUND);
        Mockito.when(artPieceRepository.findByArtId(anyInt())).thenReturn(null);
        Mockito.when(mapper.mapNoArtPieceFoundResponse(anyString())).thenReturn(response);
        ArtPieceResponse responses = artPieceOrchestratorService.getArtPiece("1", traceId);
        Assertions.assertEquals(HttpStatus.NOT_FOUND, responses.getStatus());
    }

    @Test
    public void testDeleteArtPiece(){
        ArtPieceResponse response = new ArtPieceResponse();
        response.setMessage("deletion failed");
        Mockito.when(artPieceRepository.findByArtId(anyInt())).thenReturn(new ArtPiece());
        Mockito.when(orderItemsRepository.existsByArtId(anyInt())).thenReturn(true);
        Mockito.when(artPieceRepository.save(Mockito.any(ArtPiece.class))).thenReturn(null);
        Mockito.when(mapper.mapDeleteFailedResponse(anyString())).thenReturn(response);
        ArtPieceResponse artPieceResponse = artPieceOrchestratorService.deleteArtPiece("1", traceId);
        Assertions.assertEquals("deletion failed", artPieceResponse.getMessage());
    }

    @Test
    public void testDeleteArtPieceWithNoOrderExists(){
        ArtPieceResponse response = new ArtPieceResponse();
        response.setMessage("deleted successfully");
        Mockito.when(artPieceRepository.findByArtId(anyInt())).thenReturn(new ArtPiece());
        Mockito.when(orderItemsRepository.existsByArtId(anyInt())).thenReturn(false);
        Mockito.when(mapper.mapDeleteSuccessResponse(anyString())).thenReturn(response);
        ArtPieceResponse artPieceResponse = artPieceOrchestratorService.deleteArtPiece("1", traceId);
        Assertions.assertEquals("deleted successfully", artPieceResponse.getMessage());
    }

    @Test
    public void testDeleteArtPieceWithNullArtPiece(){
        ArtPieceResponse response = new ArtPieceResponse();
        response.setMessage("no art found");
        Mockito.when(artPieceRepository.findByArtId(anyInt())).thenReturn(null);
        Mockito.when(mapper.mapNoArtPieceFoundResponse(anyString())).thenReturn(response);
        ArtPieceResponse artPieceResponse = artPieceOrchestratorService.deleteArtPiece("1", traceId);
        Assertions.assertEquals("no art found", artPieceResponse.getMessage());
    }

    private static ValidationResponse getValidationResponse() {
        ValidationResponse validationResponse = new ValidationResponse();
        ArrayValidationErrors validationErrors = new ArrayValidationErrors();
        List<ValidationError> validationErrorList = new ArrayList<>();
        ValidationError validationError = new ValidationError();
        validationError.setTextID("test ID");
        validationError.setText("Failure");
        validationErrorList.add(validationError);
        validationErrors.setErrorList(validationErrorList);
        validationResponse.setValidationErrors(validationErrors);
        return validationResponse;
    }

    private ArtPieceRequest getArtPiece(){
        ArtPieceRequest artPieceRequest = new ArtPieceRequest();
        artPieceRequest.setArtId("1");
        artPieceRequest.setTitle("test title");
        artPieceRequest.setDescription("test description");
        artPieceRequest.setPrice(BigDecimal.valueOf(10.00).setScale(2, BigDecimal.ROUND_HALF_UP));
        artPieceRequest.setCategoryId("1");
        artPieceRequest.setImageUrl("https://test.jpg");
        return artPieceRequest;
    }

    private static ArtPiece getPiece() {
        ArtPiece artPiece = new ArtPiece();
        artPiece.setArtId(1);
        artPiece.setTitle("test title");
        artPiece.setDescription("test description");
        artPiece.setPrice(10.00);
        artPiece.setImageUrl("https://test.jpg");
        return artPiece;
    }
}