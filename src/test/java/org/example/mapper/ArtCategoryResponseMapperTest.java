package org.example.mapper;

import org.example.entity.Categories;
import org.example.json.ArrayValidationErrors;
import org.example.json.ArtCategoryResponse;
import org.example.json.ValidationError;
import org.example.json.ValidationResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@ExtendWith({MockitoExtension.class})
class ArtCategoryResponseMapperTest {

    @InjectMocks
    ArtCategoryResponseMapper mapper;
    String traceId = UUID.randomUUID().toString();

    @Test
    public void testMapSuccessResponse(){
        ArtCategoryResponse response = mapper.mapSuccessResponse(getCategories(),traceId);
        Assertions.assertEquals("Art Category added successfully..!!",response.getMessage());
    }

    @Test
    public void testMapUpdateSuccessResponse(){
        ArtCategoryResponse response = mapper.mapUpdateSuccessResponse(getCategories(),traceId);
        Assertions.assertEquals("Art Category updated successfully..!!",response.getMessage());
    }

    @Test
    public void testMapDeleteSuccessResponse(){
        ArtCategoryResponse response = mapper.mapDeleteSuccessResponse(traceId);
        Assertions.assertEquals("Art Category deleted successfully...!!!",response.getMessage());
    }

    @Test
    public void testMapDeleteFailedResponse(){
        ArtCategoryResponse response = mapper.mapDeleteFailedResponse(traceId);
        Assertions.assertEquals("Cannot delete category as it has associated art pieces",response.getMessage());
    }

    @Test
    public void testMapNoCategoryFoundResponse(){
        ArtCategoryResponse response = mapper.mapNoCategoryFoundResponse(traceId);
        Assertions.assertEquals("No category found",response.getMessage());
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
        ArtCategoryResponse response = mapper.mapValidationErrorResponse(validationResponse, traceId);
        Assertions.assertEquals("Field validation failed",response.getMessage());
    }

    private Categories getCategories() {
        Categories categories = new Categories();
        categories.setCategoryId(1);
        categories.setName("test category");
        categories.setDescription("test description");
        return categories;
    }
}