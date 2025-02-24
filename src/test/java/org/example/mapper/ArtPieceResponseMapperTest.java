package org.example.mapper;

import org.example.entity.ArtPiece;
import org.example.entity.Categories;
import org.example.json.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class ArtPieceResponseMapperTest {

    @InjectMocks
    ArtPieceResponseMapper mapper;
    String traceId = UUID.randomUUID().toString();

    @Test
    public void testMapSuccessResponse(){
        ArtPieceResponse response = mapper.mapSuccessResponse(getArtPiece(), traceId);
        Assertions.assertEquals("Art Piece added successfully..!!", response.getMessage());
    }

    @Test
    public void testMapUpdateSuccessResponse(){
        ArtPieceResponse response = mapper.mapUpdateSuccessResponse(getArtPiece(), traceId);
        Assertions.assertEquals("Art Piece updated successfully..!!", response.getMessage());
    }

    @Test
    public void testMapNoCategoryFoundResponse(){
        ArtPieceResponse response = mapper.mapNoCategoryFoundResponse(traceId);
        Assertions.assertEquals("No category found", response.getMessage());
    }

    @Test
    public void testMapNoArtPieceFoundResponse(){
        ArtPieceResponse response = mapper.mapNoArtPieceFoundResponse(traceId);
        Assertions.assertEquals("No art piece found", response.getMessage());
    }

    @Test
    public void testMapDeleteSuccessResponse(){
        ArtPieceResponse response = mapper.mapDeleteSuccessResponse(traceId);
        Assertions.assertEquals("Art Piece deleted successfully...!!!", response.getMessage());
    }

    @Test
    public void testMapDeleteFailedResponse(){
        ArtPieceResponse response = mapper.mapDeleteFailedResponse(traceId);
        Assertions.assertEquals("Cannot delete art piece as it has been ordered, therefore set it as inactive instead", response.getMessage());
    }

    @Test
    public void testMapValidationErrorResponse(){
        ValidationResponse validationResponse = new ValidationResponse();
        ArrayValidationErrors arrayValidationErrors = new ArrayValidationErrors();
        List<ValidationError> validationErrors = new ArrayList<>();
        ValidationError validationError = new ValidationError();
        validationError.setTextID("error");
        validationError.setText("test error");
        validationErrors.add(validationError);
        arrayValidationErrors.setErrorList(validationErrors);
        validationResponse.setValidationErrors(arrayValidationErrors);
        ArtPieceResponse response = mapper.mapValidationErrorResponse(validationResponse, traceId);
        Assertions.assertEquals("Field validation failed",response.getMessage());
    }

    public ArtPiece getArtPiece(){
        ArtPiece artPiece = new ArtPiece();
        Categories categories = new Categories();
        categories.setName("test name");
        artPiece.setArtId(1);
        artPiece.setTitle("test title");
        artPiece.setDescription("test description");
        artPiece.setImageUrl("https://test.png");
        artPiece.setPrice(10.00);
        artPiece.setArtist("test Artist");
        artPiece.setYear(2022);
        artPiece.setDimensions("20 * 20 inches");
        artPiece.setMedium("test medium");
        artPiece.setCategory(categories);
        return artPiece;
    }

}