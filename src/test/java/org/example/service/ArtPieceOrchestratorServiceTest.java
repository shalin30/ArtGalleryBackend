package org.example.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.Uploader;
import org.example.entity.ArtPiece;
import org.example.entity.Categories;
import org.example.json.*;
import org.example.mapper.ArtPieceResponseMapper;
import org.example.repository.ArtCategoryRepository;
import org.example.repository.ArtPieceRepository;
import org.example.repository.OrderItemsRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

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

    @Mock
    private Cloudinary cloudinary;

    @Mock
    private Uploader uploader;

    @InjectMocks
    ArtPieceOrchestratorService artPieceOrchestratorService;
    String traceId = UUID.randomUUID().toString();

    private static final Integer ART_ID = 123;
    private static final String IMAGE_URL = "https://res.cloudinary.com/demo/image/upload/sample.jpg";

    private ArtPiece artPiece;
    private MultipartFile multipartFile;
    private Map<String, Object> uploadResult;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Initializes @Mock annotated fields
    }

    @Test
    public void testInsertArtPiece(){
        ArtPieceResponse artPieceResponse = new ArtPieceResponse();
        artPieceResponse.setMessage("Success");
        when(artPieceFieldValidationService.artPieceFieldValidation(any(ArtPieceRequest.class), anyString())).thenReturn(new ValidationResponse());
        when(artCategoryRepository.findByCategoryId(anyInt())).thenReturn(new Categories());
        when(artPieceRepository.save(Mockito.any(ArtPiece.class))).thenReturn(null);
        when(mapper.mapSuccessResponse(any(ArtPiece.class), anyString())).thenReturn(artPieceResponse);
        ArtPieceResponse response = artPieceOrchestratorService.insertArtPiece(getArtPiece(), traceId);
        assertEquals("Success", response.getMessage());
    }

    @Test
    public void testInsertArtPieceWithNullCategory(){
        ArtPieceResponse artPieceResponse = new ArtPieceResponse();
        artPieceResponse.setMessage("no category found");
        when(artPieceFieldValidationService.artPieceFieldValidation(any(ArtPieceRequest.class), anyString())).thenReturn(new ValidationResponse());
        when(artCategoryRepository.findByCategoryId(anyInt())).thenReturn(null);
        when(mapper.mapNoCategoryFoundResponse(anyString())).thenReturn(artPieceResponse);
        ArtPieceResponse response = artPieceOrchestratorService.insertArtPiece(getArtPiece(), traceId);
        assertEquals("no category found", response.getMessage());
    }

    @Test
    public void testInsertArtPieceWithValidationError(){
        ArtPieceResponse artCategoryResponse = new ArtPieceResponse();
        ValidationResponse validationResponse = getValidationResponse();
        artCategoryResponse.setValidationResponse(validationResponse.toString());
        when(artPieceFieldValidationService.artPieceFieldValidation(any(ArtPieceRequest.class), anyString())).thenReturn(validationResponse);
        when(mapper.mapValidationErrorResponse(any(ValidationResponse.class), anyString())).thenReturn(artCategoryResponse);
        ArtPieceResponse response = artPieceOrchestratorService.insertArtPiece(getArtPiece(), traceId);
        Assertions.assertTrue(response.getValidationResponse().contains("Failure"));
    }

    @Test
    public void testUpdateArtPiece(){
        ArtPieceResponse artPieceResponse = new ArtPieceResponse();
        artPieceResponse.setMessage("Success");
        when(artPieceFieldValidationService.updateArtPieceFieldValidation(any(ArtPieceRequest.class), anyString())).thenReturn(new ValidationResponse());
        when(artPieceRepository.findByArtId(anyInt())).thenReturn(new ArtPiece());
        when(artCategoryRepository.findByCategoryId(anyInt())).thenReturn(new Categories());
        when(artPieceRepository.save(Mockito.any(ArtPiece.class))).thenReturn(null);
        when(mapper.mapUpdateSuccessResponse(any(ArtPiece.class), anyString())).thenReturn(artPieceResponse);
        ArtPieceResponse response = artPieceOrchestratorService.updateArtPiece(getArtPiece(), traceId);
        assertEquals("Success", response.getMessage());
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
        when(artPieceFieldValidationService.updateArtPieceFieldValidation(any(ArtPieceRequest.class), anyString())).thenReturn(new ValidationResponse());
        when(artPieceRepository.findByArtId(anyInt())).thenReturn(new ArtPiece());
        when(artPieceRepository.save(Mockito.any(ArtPiece.class))).thenReturn(null);
        when(mapper.mapUpdateSuccessResponse(any(ArtPiece.class), anyString())).thenReturn(artPieceResponse);
        ArtPieceResponse response = artPieceOrchestratorService.updateArtPiece(artPieceRequest, traceId);
        assertEquals("Success", response.getMessage());
    }

    @Test
    public void testUpdateArtPieceWithNullCategory(){
        ArtPieceResponse artPieceResponse = new ArtPieceResponse();
        artPieceResponse.setMessage("no category found");
        when(artPieceFieldValidationService.updateArtPieceFieldValidation(any(ArtPieceRequest.class), anyString())).thenReturn(new ValidationResponse());
        when(artPieceRepository.findByArtId(anyInt())).thenReturn(new ArtPiece());
        when(artCategoryRepository.findByCategoryId(anyInt())).thenReturn(null);
        when(mapper.mapNoCategoryFoundResponse(anyString())).thenReturn(artPieceResponse);
        ArtPieceResponse response = artPieceOrchestratorService.updateArtPiece(getArtPiece(), traceId);
        assertEquals("no category found", response.getMessage());
    }

    @Test
    public void testUpdateArtPieceWithNullArtPiece(){
        ArtPieceResponse artPieceResponse = new ArtPieceResponse();
        artPieceResponse.setMessage("no art piece found");
        when(artPieceFieldValidationService.updateArtPieceFieldValidation(any(ArtPieceRequest.class), anyString())).thenReturn(new ValidationResponse());
        when(artPieceRepository.findByArtId(anyInt())).thenReturn(null);
        when(mapper.mapNoArtPieceFoundResponse(anyString())).thenReturn(artPieceResponse);
        ArtPieceResponse response = artPieceOrchestratorService.updateArtPiece(getArtPiece(), traceId);
        assertEquals("no art piece found", response.getMessage());
    }

    @Test
    public void testUpdateArtPieceWithValidationError(){
        ArtPieceResponse artCategoryResponse = new ArtPieceResponse();
        ValidationResponse validationResponse = getValidationResponse();
        artCategoryResponse.setValidationResponse(validationResponse.toString());
        when(artPieceFieldValidationService.updateArtPieceFieldValidation(any(ArtPieceRequest.class), anyString())).thenReturn(validationResponse);
        when(mapper.mapValidationErrorResponse(any(ValidationResponse.class), anyString())).thenReturn(artCategoryResponse);
        ArtPieceResponse response = artPieceOrchestratorService.updateArtPiece(getArtPiece(), traceId);
        Assertions.assertTrue(response.getValidationResponse().contains("Failure"));
    }

    @Test
    public void testGetArtPieces(){
        List<ArtPiece> artPieces = new ArrayList<>();
        ArtPiece artPiece = getPiece();
        artPieces.add(artPiece);
        when(artPieceRepository.findArtPiecesByCategoryId(anyInt())).thenReturn(artPieces);
        List<ArtPieceResponse> responses = artPieceOrchestratorService.getArtPieces("1", traceId);
        assertEquals(1, responses.get(0).getArtId());
    }

    @Test
    public void testGetArtPiece(){
        ArtPiece artPiece = getPiece();
        when(artPieceRepository.findByArtId(anyInt())).thenReturn(artPiece);
        ArtPieceResponse responses = artPieceOrchestratorService.getArtPiece("1", traceId);
        assertEquals(1, responses.getArtId());
    }

    @Test
    public void testGetArtPiecesWithNullValues(){
        when(artPieceRepository.findArtPiecesByCategoryId(anyInt())).thenReturn(null);
        List<ArtPieceResponse> responses = artPieceOrchestratorService.getArtPieces("1", traceId);
        assertEquals(HttpStatus.NOT_FOUND, responses.get(0).getStatus());
    }

    @Test
    public void testGetArtPieceWithNullValues(){
        ArtPieceResponse response = new ArtPieceResponse();
        response.setStatus(HttpStatus.NOT_FOUND);
        when(artPieceRepository.findByArtId(anyInt())).thenReturn(null);
        when(mapper.mapNoArtPieceFoundResponse(anyString())).thenReturn(response);
        ArtPieceResponse responses = artPieceOrchestratorService.getArtPiece("1", traceId);
        assertEquals(HttpStatus.NOT_FOUND, responses.getStatus());
    }

    @Test
    public void testDeleteArtPiece(){
        ArtPieceResponse response = new ArtPieceResponse();
        response.setMessage("deletion failed");
        when(artPieceRepository.findByArtId(anyInt())).thenReturn(new ArtPiece());
        when(orderItemsRepository.existsByArtId(anyInt())).thenReturn(true);
        when(artPieceRepository.save(Mockito.any(ArtPiece.class))).thenReturn(null);
        when(mapper.mapDeleteFailedResponse(anyString())).thenReturn(response);
        ArtPieceResponse artPieceResponse = artPieceOrchestratorService.deleteArtPiece("1", traceId);
        assertEquals("deletion failed", artPieceResponse.getMessage());
    }

    @Test
    public void testDeleteArtPieceWithNoOrderExists(){
        ArtPieceResponse response = new ArtPieceResponse();
        response.setMessage("deleted successfully");
        when(artPieceRepository.findByArtId(anyInt())).thenReturn(new ArtPiece());
        when(orderItemsRepository.existsByArtId(anyInt())).thenReturn(false);
        when(mapper.mapDeleteSuccessResponse(anyString())).thenReturn(response);
        ArtPieceResponse artPieceResponse = artPieceOrchestratorService.deleteArtPiece("1", traceId);
        assertEquals("deleted successfully", artPieceResponse.getMessage());
    }

    @Test
    public void testDeleteArtPieceWithNullArtPiece(){
        ArtPieceResponse response = new ArtPieceResponse();
        response.setMessage("no art found");
        when(artPieceRepository.findByArtId(anyInt())).thenReturn(null);
        when(mapper.mapNoArtPieceFoundResponse(anyString())).thenReturn(response);
        ArtPieceResponse artPieceResponse = artPieceOrchestratorService.deleteArtPiece("1", traceId);
        assertEquals("no art found", artPieceResponse.getMessage());
    }

    @Test
    public void testUploadImageAndSetUrl() throws IOException {
        ArtPiece artPiece = new ArtPiece();
        artPiece.setArtId(ART_ID);

        MockMultipartFile multipartFile = new MockMultipartFile(
                "image.jpg",
                "image.jpg",
                "image/jpeg",
                "test image content".getBytes(StandardCharsets.UTF_8)
        );

        Map<String, Object> uploadResult = new HashMap<>();
        uploadResult.put("url", IMAGE_URL);

        Uploader mockUploader = mock(Uploader.class);
        when(cloudinary.uploader()).thenReturn(mockUploader);
        when(mockUploader.upload(any(byte[].class), anyMap())).thenReturn(uploadResult);

        when(artPieceRepository.findByArtId(ART_ID)).thenReturn(artPiece);

        String result = artPieceOrchestratorService.uploadImageAndSetUrl(ART_ID, multipartFile);

        assertEquals(IMAGE_URL, result);
        assertEquals(IMAGE_URL, artPiece.getImageUrl());

        ArgumentCaptor<Map<String, Object>> optionsCaptor = ArgumentCaptor.forClass(Map.class);
        verify(mockUploader).upload(any(byte[].class), optionsCaptor.capture());
        Map<String, Object> capturedOptions = optionsCaptor.getValue();
        assertEquals(10485760, capturedOptions.get("max_file_size"));

        verify(artPieceRepository).save(artPiece);
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
        artPieceRequest.setArtist("test Artist");
        artPieceRequest.setYear("2022");
        artPieceRequest.setDimensions("20 * 20 inches");
        artPieceRequest.setMedium("test medium");
        return artPieceRequest;
    }

    private static ArtPiece getPiece() {
        ArtPiece artPiece = new ArtPiece();
        Categories categories = new Categories();
        categories.setName("test name");
        artPiece.setArtId(1);
        artPiece.setTitle("test title");
        artPiece.setDescription("test description");
        artPiece.setPrice(10.00);
        artPiece.setImageUrl("https://test.jpg");
        artPiece.setArtist("test Artist");
        artPiece.setYear(2022);
        artPiece.setDimensions("20 * 20 inches");
        artPiece.setMedium("test medium");
        artPiece.setCategory(categories);
        return artPiece;
    }
}