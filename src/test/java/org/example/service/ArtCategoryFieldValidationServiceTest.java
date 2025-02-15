package org.example.service;

import org.example.json.ArtCategoryRequest;
import org.example.json.ValidationResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ArtCategoryFieldValidationServiceTest {

    @InjectMocks
    ArtCategoryFieldValidationService artCategoryFieldValidationService;
    ArtCategoryRequest artCategoryRequest;
    String traceId = UUID.randomUUID().toString();

    @Test
    public void testArtCategoryFieldValidation(){
        artCategoryRequest = new ArtCategoryRequest();
        artCategoryRequest.setName("test name");
        artCategoryRequest.setDescription("test description");

        ValidationResponse validationResponse = artCategoryFieldValidationService.artCategoryFieldValidation(artCategoryRequest, traceId);
        Assertions.assertEquals(Collections.EMPTY_LIST, validationResponse.getValidationErrors().getErrorList());
    }

    @Test
    public void testArtCategoryFieldValidationWithNullValue(){
        artCategoryRequest = new ArtCategoryRequest();
        artCategoryRequest.setName(null);
        artCategoryRequest.setDescription(null);

        ValidationResponse validationResponse = artCategoryFieldValidationService.artCategoryFieldValidation(artCategoryRequest, traceId);
        Assertions.assertTrue(validationResponse.getValidationErrors().getErrorList().toString().contains("Name is required"));
    }

    @Test
    public void testUpdateArtCategoryFieldValidation(){
        artCategoryRequest = new ArtCategoryRequest();
        artCategoryRequest.setName("test name");
        artCategoryRequest.setDescription("test description");

        ValidationResponse validationResponse = artCategoryFieldValidationService.updateArtCategoryFieldValidation(artCategoryRequest, traceId);
        Assertions.assertEquals(Collections.EMPTY_LIST, validationResponse.getValidationErrors().getErrorList());
    }

    @Test
    public void testUpdateArtCategoryFieldValidationWithNullValues(){
        artCategoryRequest = new ArtCategoryRequest();
        artCategoryRequest.setName(null);
        artCategoryRequest.setDescription(null);

        ValidationResponse validationResponse = artCategoryFieldValidationService.updateArtCategoryFieldValidation(artCategoryRequest, traceId);
        Assertions.assertEquals(Collections.EMPTY_LIST, validationResponse.getValidationErrors().getErrorList());
    }
}