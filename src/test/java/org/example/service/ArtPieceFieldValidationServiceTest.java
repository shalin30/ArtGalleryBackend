package org.example.service;

import org.example.json.ArtPieceRequest;
import org.example.json.ValidationResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.UUID;


@ExtendWith(MockitoExtension.class)
class ArtPieceFieldValidationServiceTest {

    @InjectMocks
    ArtPieceFieldValidationService artPieceFieldValidationService;
    String traceId = UUID.randomUUID().toString();

    @Test
    public void testArtPieceFieldValidation(){
        ValidationResponse validationResponse = artPieceFieldValidationService.artPieceFieldValidation(getArtPiece(), traceId);
        Assertions.assertEquals(Collections.EMPTY_LIST, validationResponse.getValidationErrors().getErrorList());
    }

    @Test
    public void testUpdateArtPieceFieldValidation(){
        ValidationResponse validationResponse = artPieceFieldValidationService.updateArtPieceFieldValidation(getArtPiece(), traceId);
        Assertions.assertEquals(Collections.EMPTY_LIST, validationResponse.getValidationErrors().getErrorList());
    }

    @Test
    public void testUpdateArtPieceFieldValidationWithNullValues(){
        ArtPieceRequest artPieceRequest = new ArtPieceRequest();
        artPieceRequest.setArtId(null);
        artPieceRequest.setTitle(null);
        artPieceRequest.setDescription(null);
        artPieceRequest.setPrice(null);
        artPieceRequest.setCategoryId(null);
        artPieceRequest.setImageUrl(null);
        artPieceRequest.setArtist(null);
        artPieceRequest.setYear(null);
        artPieceRequest.setDimensions(null);
        artPieceRequest.setMedium(null);
        ValidationResponse validationResponse = artPieceFieldValidationService.updateArtPieceFieldValidation(artPieceRequest, traceId);
        Assertions.assertEquals(Collections.EMPTY_LIST, validationResponse.getValidationErrors().getErrorList());
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
}