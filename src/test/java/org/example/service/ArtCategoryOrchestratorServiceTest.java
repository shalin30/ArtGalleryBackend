package org.example.service;

import org.example.entity.ArtPiece;
import org.example.entity.Categories;
import org.example.json.*;
import org.example.mapper.ArtCategoryResponseMapper;
import org.example.repository.ArtCategoryRepository;
import org.example.repository.ArtPieceRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.*;

@ExtendWith(MockitoExtension.class)
class ArtCategoryOrchestratorServiceTest {

    @Mock
    ArtCategoryRepository artCategoryRepository;

    @Mock
    ArtPieceRepository artPieceRepository;

    @Mock
    ArtCategoryFieldValidationService artCategoryFieldValidationService;

    @Mock
    ArtCategoryResponseMapper mapper;
    
    @InjectMocks
    ArtCategoryOrchestratorService artCategoryOrchestratorService;
    ArtCategoryRequest artCategoryRequest;
    String traceId = UUID.randomUUID().toString();
    
    
    @Test
    public void testInsertArtCategory(){
        ArtCategoryResponse artCategoryResponse = new ArtCategoryResponse();
        artCategoryResponse.setMessage("Success");
        Mockito.when(artCategoryFieldValidationService.artCategoryFieldValidation(any(ArtCategoryRequest.class), anyString())).thenReturn(new ValidationResponse());
        Mockito.when(artCategoryRepository.save(Mockito.any(Categories.class))).thenReturn(null);
        Mockito.when(mapper.mapSuccessResponse(any(Categories.class), anyString())).thenReturn(artCategoryResponse);
        ArtCategoryResponse response = artCategoryOrchestratorService.insertArtCategory(getArtCategory(), traceId);
        Assertions.assertEquals("Success", response.getMessage());
    }

    @Test
    public void testInsertArtCategoryWithValidationError(){
        ArtCategoryResponse artCategoryResponse = new ArtCategoryResponse();
        ValidationResponse validationResponse = getValidationResponse();
        artCategoryResponse.setValidationResponse(validationResponse.toString());
        Mockito.when(artCategoryFieldValidationService.artCategoryFieldValidation(any(ArtCategoryRequest.class), anyString())).thenReturn(validationResponse);
        Mockito.when(mapper.mapValidationErrorResponse(any(ValidationResponse.class), anyString())).thenReturn(artCategoryResponse);
        ArtCategoryResponse response = artCategoryOrchestratorService.insertArtCategory(getArtCategory(), traceId);
        Assertions.assertTrue(response.getValidationResponse().contains("Failure"));
    }

    @Test
    public void testUpdateArtCategory(){
        ArtCategoryResponse artCategoryResponse = new ArtCategoryResponse();
        artCategoryResponse.setMessage("Success");
        Mockito.when(artCategoryFieldValidationService.updateArtCategoryFieldValidation(any(ArtCategoryRequest.class), anyString())).thenReturn(new ValidationResponse());
        Mockito.when(artCategoryRepository.findByCategoryId(anyInt())).thenReturn(new Categories());
        Mockito.when(artCategoryRepository.save(Mockito.any(Categories.class))).thenReturn(null);
        Mockito.when(mapper.mapUpdateSuccessResponse(any(Categories.class), anyString())).thenReturn(artCategoryResponse);
        ArtCategoryResponse response = artCategoryOrchestratorService.updateArtCategory(getArtCategory(), traceId);
        Assertions.assertEquals("Success", response.getMessage());
    }

    @Test
    public void testUpdateArtCategoryWithNullCategoryFields(){
        ArtCategoryResponse artCategoryResponse = new ArtCategoryResponse();
        artCategoryResponse.setMessage("Success");

        ArtCategoryRequest request = new ArtCategoryRequest();
        request.setCategoryId("1");
        request.setName(null);
        request.setDescription(null);

        Mockito.when(artCategoryFieldValidationService.updateArtCategoryFieldValidation(any(ArtCategoryRequest.class), anyString())).thenReturn(new ValidationResponse());
        Mockito.when(artCategoryRepository.findByCategoryId(anyInt())).thenReturn(new Categories());
        Mockito.when(artCategoryRepository.save(Mockito.any(Categories.class))).thenReturn(null);
        Mockito.when(mapper.mapUpdateSuccessResponse(any(Categories.class), anyString())).thenReturn(artCategoryResponse);
        ArtCategoryResponse response = artCategoryOrchestratorService.updateArtCategory(request, traceId);
        Assertions.assertEquals("Success", response.getMessage());
    }

    @Test
    public void testUpdateArtCategoryWithNoCategoryFound(){
        ArtCategoryResponse artCategoryResponse = new ArtCategoryResponse();
        artCategoryResponse.setMessage("No Category Found");
        Mockito.when(artCategoryFieldValidationService.updateArtCategoryFieldValidation(any(ArtCategoryRequest.class), anyString())).thenReturn(new ValidationResponse());
        Mockito.when(artCategoryRepository.findByCategoryId(anyInt())).thenReturn(null);
        Mockito.when(mapper.mapNoCategoryFoundResponse(anyString())).thenReturn(artCategoryResponse);
        ArtCategoryResponse response = artCategoryOrchestratorService.updateArtCategory(getArtCategory(), traceId);
        Assertions.assertEquals("No Category Found", response.getMessage());
    }

    @Test
    public void testUpdateArtCategoryWithValidationError(){
        ArtCategoryResponse artCategoryResponse = new ArtCategoryResponse();
        ValidationResponse validationResponse = getValidationResponse();
        artCategoryResponse.setValidationResponse(validationResponse.toString());
        Mockito.when(artCategoryFieldValidationService.updateArtCategoryFieldValidation(any(ArtCategoryRequest.class), anyString())).thenReturn(validationResponse);
        Mockito.when(mapper.mapValidationErrorResponse(any(ValidationResponse.class), anyString())).thenReturn(artCategoryResponse);
        ArtCategoryResponse response = artCategoryOrchestratorService.updateArtCategory(getArtCategory(), traceId);
        Assertions.assertTrue(response.getValidationResponse().contains("Failure"));
    }

    @Test
    public void testGetArtCategories(){
        List<Categories> categoriesList = new ArrayList<>();
        Categories categories = new Categories();
        categories.setCategoryId(1);
        categories.setName("test name");
        categories.setDescription("test description");
        categoriesList.add(categories);
        Mockito.when(artCategoryRepository.findAll()).thenReturn(categoriesList);
        List<ArtCategoryResponse> responses = artCategoryOrchestratorService.getArtCategories(traceId);
        Assertions.assertEquals("test name", responses.get(0).getName());
    }

    @Test
    public void testGetArtCategoriesWithNullResponse(){
        Mockito.when(artCategoryRepository.findAll()).thenReturn(null);
        List<ArtCategoryResponse> responses = artCategoryOrchestratorService.getArtCategories(traceId);
        Assertions.assertEquals(HttpStatus.NOT_FOUND, responses.get(0).getStatus());
    }

    @Test
    public void testDeleteArtCategories(){
        Categories categories = new Categories();
        categories.setCategoryId(1);
        categories.setName("test name");
        categories.setDescription("test description");

        List<ArtPiece> artPieces = new ArrayList<>();
        ArtPiece artPiece = new ArtPiece();
        artPiece.setTitle("test title");
        artPieces.add(artPiece);

        ArtCategoryResponse artCategoryResponse = new ArtCategoryResponse();
        artCategoryResponse.setMessage("deletion failed");

        Mockito.when(artCategoryRepository.findByCategoryId(anyInt())).thenReturn(categories);
        Mockito.when(artPieceRepository.findArtPiecesByCategoryId(anyInt())).thenReturn(artPieces);
        Mockito.when(mapper.mapDeleteFailedResponse(traceId)).thenReturn(artCategoryResponse);
        ArtCategoryResponse response = artCategoryOrchestratorService.deleteArtCategory("1",traceId);
        Assertions.assertEquals("deletion failed", response.getMessage());
    }

    @Test
    public void testDeleteArtCategoriesWithNullArt(){
        Categories categories = new Categories();
        categories.setCategoryId(1);
        categories.setName("test name");
        categories.setDescription("test description");

        ArtCategoryResponse artCategoryResponse = new ArtCategoryResponse();
        artCategoryResponse.setMessage("deleted successfully");

        Mockito.when(artCategoryRepository.findByCategoryId(anyInt())).thenReturn(categories);
        Mockito.when(artPieceRepository.findArtPiecesByCategoryId(anyInt())).thenReturn(null);
        Mockito.when(mapper.mapDeleteSuccessResponse(traceId)).thenReturn(artCategoryResponse);
        ArtCategoryResponse response = artCategoryOrchestratorService.deleteArtCategory("1",traceId);
        Assertions.assertEquals("deleted successfully", response.getMessage());
    }

    @Test
    public void testDeleteArtCategoriesWithEmptyArt(){
        Categories categories = new Categories();
        categories.setCategoryId(1);
        categories.setName("test name");
        categories.setDescription("test description");

        ArtCategoryResponse artCategoryResponse = new ArtCategoryResponse();
        artCategoryResponse.setMessage("deleted successfully");

        Mockito.when(artCategoryRepository.findByCategoryId(anyInt())).thenReturn(categories);
        Mockito.when(artPieceRepository.findArtPiecesByCategoryId(anyInt())).thenReturn(new ArrayList<>());
        Mockito.when(mapper.mapDeleteSuccessResponse(traceId)).thenReturn(artCategoryResponse);
        ArtCategoryResponse response = artCategoryOrchestratorService.deleteArtCategory("1",traceId);
        Assertions.assertEquals("deleted successfully", response.getMessage());
    }

    @Test
    public void testDeleteArtCategoriesWithNullCategory(){
        ArtCategoryResponse artCategoryResponse = new ArtCategoryResponse();
        artCategoryResponse.setMessage("no category found");

        Mockito.when(artCategoryRepository.findByCategoryId(anyInt())).thenReturn(null);
        Mockito.when(mapper.mapNoCategoryFoundResponse(traceId)).thenReturn(artCategoryResponse);
        ArtCategoryResponse response = artCategoryOrchestratorService.deleteArtCategory("1",traceId);
        Assertions.assertEquals("no category found", response.getMessage());
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

    private ArtCategoryRequest getArtCategory() {
        artCategoryRequest = new ArtCategoryRequest();
        artCategoryRequest.setCategoryId("1");
        artCategoryRequest.setName("test name");
        artCategoryRequest.setDescription("test description");
        return artCategoryRequest;
    }
}