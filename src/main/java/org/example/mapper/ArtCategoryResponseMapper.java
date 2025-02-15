package org.example.mapper;

import lombok.extern.slf4j.Slf4j;
import org.example.entity.Categories;
import org.example.json.ArtCategoryResponse;
import org.example.json.ValidationResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ArtCategoryResponseMapper {

    public ArtCategoryResponse mapSuccessResponse(Categories categories, String traceId) {
        log.info("map success response started, traceId: {}", traceId);
        ArtCategoryResponse response = new ArtCategoryResponse();
        response.setCategoryId(categories.getCategoryId().toString());
        response.setName(categories.getName());
        response.setDescription(categories.getDescription());
        response.setStatus(HttpStatus.OK);
        response.setMessage("Art Category added successfully..!!");
        log.info("map success response ended, traceId: {}", traceId);
        return response;
    }

    public ArtCategoryResponse mapValidationErrorResponse(ValidationResponse validationResponse, String traceId) {
        log.info("map validation error response started, traceId: {}", traceId);
        ArtCategoryResponse response = new ArtCategoryResponse();
        response.setValidationResponse(validationResponse.getValidationErrors().getErrorList().toString());
        response.setStatus(HttpStatus.BAD_REQUEST);
        response.setMessage("Field validation failed");
        log.info("map validation error response ended, traceId: {}", traceId);
        return response;
    }

    public ArtCategoryResponse mapUpdateSuccessResponse(Categories categories, String traceId) {
        log.info("map update success response started, traceId: {}", traceId);
        ArtCategoryResponse response = new ArtCategoryResponse();
        response.setCategoryId(categories.getCategoryId().toString());
        response.setName(categories.getName());
        response.setDescription(categories.getDescription());
        response.setStatus(HttpStatus.OK);
        response.setMessage("Art Category updated successfully..!!");
        log.info("map update success response ended, traceId: {}", traceId);
        return response;
    }

    public ArtCategoryResponse mapDeleteSuccessResponse(String traceId) {
        log.info("map delete success response started, traceId: {}", traceId);
        ArtCategoryResponse response = new ArtCategoryResponse();
        response.setStatus(HttpStatus.OK);
        response.setMessage("Art Category deleted successfully...!!!");
        log.info("map delete success response ended, traceId: {}", traceId);
        return response;
    }

    public ArtCategoryResponse mapDeleteFailedResponse(String traceId) {
        log.info("map delete failed response started, traceId: {}", traceId);
        ArtCategoryResponse response = new ArtCategoryResponse();
        response.setStatus(HttpStatus.CONFLICT);
        response.setMessage("Cannot delete category as it has associated art pieces");
        log.info("map delete failed response ended, traceId: {}", traceId);
        return response;
    }

    public ArtCategoryResponse mapNoCategoryFoundResponse(String traceId) {
        log.info("map no category found response started, traceId: {}", traceId);
        ArtCategoryResponse response = new ArtCategoryResponse();
        response.setStatus(HttpStatus.NOT_FOUND);
        response.setMessage("No category found");
        log.info("map no category found response ended, traceId: {}", traceId);
        return response;
    }
}
